package com.giladdev.rickyandmarty.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.giladdev.rickyandmarty.Util.ImageFetcher
import com.giladdev.rickyandmarty.Util.bitMapToString
import com.giladdev.rickyandmarty.di.CharactersServicesComponent
import com.giladdev.rickyandmarty.model.*
import com.squareup.picasso.Picasso
import io.reactivex.Observable.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

enum class ConnectionMode{
    OFFLINE,
    ONLINE
}

class ListViewModel(application: Application): AndroidViewModel(application) {

    // To add dagger
    var charactersDB = AppDataBase.getAppDataBase(getApplication<Application>().applicationContext)

    //var charecterService = CharectersApiService()
    var charecterService = CharactersServicesComponent().service

    val characters = MutableLiveData<CharacterList>()
    var dbCharacters = ArrayList<CharecterDBEntity>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    var connMode: ConnectionMode = ConnectionMode.OFFLINE

    private val fetcher = ImageFetcher(Picasso.get())

    // RXJAVA2
    var singleImageList = ArrayList<Single<Bitmap>>()
    private val disposable = CompositeDisposable()


    fun dbsUpdateWatchDog() {
        loading.value = true

        Thread.sleep(5000)

        fromCallable {
            var isAllUpdated = true
            var isCheckFinished = false
            var tries = 0

            while ((tries < 5) && (isCheckFinished == false)) {

                for (i in dbCharacters) {
                    if (i.imageRawData == "EMPTY") {
                        isAllUpdated = false
                    }

                }
                if (isAllUpdated == true) isCheckFinished = true
                tries++
            }
            isAllUpdated
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    disposable.add(it)
                }
                .subscribe(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                        Log.d("fetchCharectersFromDB()", "Completed")
                    }

                    override fun onNext(t: Boolean) {
                        if (t == true) {
                            loading.value = false
                            countryLoadError.value = false

                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("fetchCharectersFromDB()", "Error = ${e.message}")
                    }

                })
    }

    fun refreshMoreLines() {
        var nextUrl: String? = null

        characters.value?.let {
            nextUrl = it.info.next
        }

        if (nextUrl == null) charecterService.pageNumber = "1"
        else {
            var urlParameters = Uri.parse(characters.value?.info?.next)
            var pageNum = urlParameters.getQueryParameter("page")
            charecterService.pageNumber = pageNum ?: "1"
        }

        characters.value?.run {
            fetchCharacters()
        }
    }


    fun refresh() {
        var nextUrl: String? = null

        characters.value?.let {
            nextUrl = it.info.next
        }

        if (nextUrl == null) {
            charecterService.pageNumber = "1"
            characters.value = CharacterList()
            characters.value!!.characterList = mutableListOf<Character>()
            characters.value!!.info = Info("0")
        } else {
            var urlParameters = Uri.parse(characters.value?.info?.next)
            var pageNum = urlParameters.getQueryParameter("page")
            charecterService.pageNumber = pageNum ?: "1"
        }

        if (connMode == ConnectionMode.ONLINE) {
            characters.value?.run {
                deleteAllRows()
                this.characterList.clear()
            }
            fetchCharacters()
        } else {
            // DBS Mode
            characters.value?.run {
                characterList.clear()
            }

            fetchCharectersFromDB()
        }
    }

    fun fetchCharectersFromDB() {
        loading.value = true // loading of data starting

        fromCallable({ charactersDB?.CharactersDAO()?.getCharacters() })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    disposable.add(it)
                }
                .subscribe(object : DisposableObserver<List<CharecterDBEntity>>() {
                    override fun onComplete() {
                        Log.d("fetchCharectersFromDB()", "Completed")
                    }

                    override fun onNext(t: List<CharecterDBEntity>) {
                        var charList = CharacterList()
                        charList.characterList = mutableListOf<Character>()
                        charList.info = Info("DB")
                        charList.characterList.copyDBList(t)

                        characters.value = charList
                        dbCharacters.clear()
                        dbCharacters.addAll(t)

                        countryLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("fetchCharectersFromDB()", "Error = ${e.message}")
                    }

                })

    }

    fun deleteAllRows() {
        fromCallable({
            charactersDB?.run {
                CharactersDAO().deleteAll()
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    disposable.add(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Unit>() {
                    override fun onNext(t: Unit) {

                    }

                    override fun onComplete() {
                        Log.d("deleteAllRows()", "Delete Charecters DB Completed")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("deleteAllRows()", "Error = ${e.message}")
                    }

                })

    }

    fun unSubscribe() {
        disposable.dispose()
    }

    fun updateDBSImages() {

        var observable : Single<Bitmap>?=null

        for (i in dbCharacters) {
           // LoadImageToDBS(getApplication<Application>().applicationContext, i, this)
            //   charList.characterList[i.id!!].imageRawData = dbCharacters[i.id!!].imageRawData

            observable =
                    fetcher.loadImage(i.url)
                            .doOnSubscribe {
                                disposable.add(it)
                            }

            singleImageList.add(observable)

            observable.subscribe(object : DisposableSingleObserver<Bitmap>() {
                override fun onSuccess(t: Bitmap) {
                    Log.d("Image", "onSuccess")
                    i.imageRawData = bitMapToString(t)
                    updateCharacter(i)
                }

                override fun onError(e: Throwable) {
                    Log.d("Image Loading Error",e.message)
                }

            })
      }

        Single.zip(singleImageList, { args -> Arrays.asList(args) })
                .subscribe(
                        {
                            // wait for all images to be read
                            Log.d("Zip", "Zip Success")
                            countryLoadError.value = false
                            loading.value = false
                        },{
                    countryLoadError.value = true
                    loading.value = false
                    val c = it
                }
                )

        Single.zip(singleImageList[0],singleImageList[1], BiFunction<Bitmap,Bitmap,String>(){ bitmap: Bitmap, bitmap1: Bitmap ->
            bitmap.toString()+bitmap1.toString()
        }
        ).subscribe(object : DisposableSingleObserver<String>(){
            override fun onSuccess(t: String) {
                Log.d("Zip", "Zip Test Success")
            }

            override fun onError(e: Throwable) {
                Log.d("Zip", "Zip Test Success")
            }
        })

        Log.d("End of function", "End of function")
   }

    fun updateCharacter(character: CharecterDBEntity) {
        fromCallable({ charactersDB?.CharactersDAO()?.updateCharecters(character) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    disposable.add(it)
                }
                .subscribe(object : DisposableObserver<Unit>() {
                    override fun onNext(t: Unit) {

                    }

                    override fun onComplete() {
                        Log.d("UpdateCharacter()", "Update Charecters DB Completed")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("UpdateCharacter()", "Error = ${e.message}")
                    }

                })
    }

    // update DBS if we are online
    fun updateDBS(value: CharacterList) {

        dbCharacters.clear()

        // ROOM and Retrofit have different data structures to hold the DATA
        for (i in 0..value.characterList.size - 1) {
            dbCharacters.add(CharecterDBEntity(id = value.characterList[i].id, name = value.characterList[i].name.toString(),
                    gender = value.characterList[i].gender.toString(), url = value.characterList[i].image.toString(), imageRawData = "EMPTY"))
        }

        fromCallable({ charactersDB?.CharactersDAO()?.insertAll(dbCharacters) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    disposable.add(it)
                }
                .subscribe(object : DisposableObserver<Unit>() {
                    override fun onNext(t: Unit) {

                    }

                    override fun onComplete() {
                        Log.d("updateDBS()", "Update Charecters DB Completed")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("updateDBS()", "Error = ${e.message}")
                    }

                })

    }

    private fun fetchCharacters() {
        loading.value = true // loading of data starting

        // call the API via RX java and observe
        disposable.add(
                charecterService.getCountires()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<CharacterList>() {
                            var characterListTemp = CharacterList()

                            override fun onSuccess(value: CharacterList) {
                                characters.value?.let {
                                    characterListTemp.characterList = mutableListOf<Character>()
                                    characterListTemp.info = Info(value.info.next)
                                    characterListTemp.characterList.addAll(it.characterList)
                                    characterListTemp.characterList.addAll(value.characterList)
                                    // update all characters to the new big list
                                    characterListTemp.characterList.map {t->
                                        var temp = t
                                        temp.id = temp.id?.minus(1)
                                        temp
                                    }
                                }
                                deleteAllRows()
                                updateDBS(characterListTemp)
                                updateDBSImages()
                                //dbsUpdateWatchDog()
                                characters.value = characterListTemp

                            }

                            override fun onError(e: Throwable) {
                                countryLoadError.value = true
                                loading.value = false
                            }
                        }
                        )

        )
    }

    fun MutableList<Character>.copyDBList(dblist: List<CharecterDBEntity>) {

        for (i in 0..dblist.size - 1) {
            var character = Character(dblist[i].name, dblist[i].gender, dblist[i].url, i, dblist[i].imageRawData)
            this.add(character)
        }
    }
}












