package com.giladdev.rickyandmarty.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.giladdev.rickyandmarty.Util.LoadImageToDBS
import com.giladdev.rickyandmarty.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

enum class ConnectionMode{
    OFFLINE,
    ONLINE
}

class ListViewModel(application: Application): AndroidViewModel(application) {

    var charactersDB =  AppDataBase.getAppDataBase(getApplication<Application>().applicationContext)
    var charecterService = CharectersApiService()
    val characters = MutableLiveData<CharacterList>()
    var dbCharacters = ArrayList<CharecterDBEntity>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    var connMode : ConnectionMode = ConnectionMode.OFFLINE

    private val disposable = CompositeDisposable()

    fun refreshMoreLines()
    {
        var nextUrl : String?= null

        characters.value?.let{
            nextUrl = it.info.next
        }

        if (nextUrl==null) charecterService.pageNumber="1"
        else {
            var urlParameters = Uri.parse(characters.value?.info?.next)
            var pageNum = urlParameters.getQueryParameter("page")
            charecterService.pageNumber = pageNum?:"1"
        }

        characters.value?.run {
            fetchCharacters()
        }
    }

    fun refresh(){
        var nextUrl : String?= null

        characters.value?.let{
            nextUrl = it.info.next
        }

        if (nextUrl==null)
        {
            charecterService.pageNumber="1"
            characters.value = CharacterList()
            characters.value!!.characterList = mutableListOf<Character>()
            characters.value!!.info = Info("0")
        }
        else {
            var urlParameters = Uri.parse(characters.value?.info?.next)
            var pageNum = urlParameters.getQueryParameter("page")
            charecterService.pageNumber = pageNum?:"1"
        }

        if (connMode==ConnectionMode.ONLINE) {
            characters.value?.run{
                deleteAllRows()
                this.characterList.clear()
            }
            fetchCharacters()
        }
        else{
            characters.value?.run{
                characterList.clear()
            }

            fetchCharectersFromDB()
        }
    }

    fun fetchCharectersFromDB(){
        loading.value = true // loading of data starting

        Observable.fromCallable( {charactersDB?.CharactersDAO()?.getCharacters()})
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                disposable.add(it)
            }
            .subscribe(object : DisposableObserver<List<CharecterDBEntity>>(){
                override fun onComplete() {
                    Log.d("fetchCharectersFromDB()","Completed")
                }

                override fun onNext(t: List<CharecterDBEntity>) {
                    var charList = CharacterList()
                    charList.characterList = mutableListOf<Character>()
                    charList.info = Info("DB")
                    charList.characterList.copyDBList(t)

                    characters.value = charList

                    countryLoadError.value = false
                    loading.value = false
                }

                override fun onError(e: Throwable) {
                    Log.d("fetchCharectersFromDB()","Error = ${e.message}")
                }

            })

    }

    fun deleteAllRows()
    {
        Observable.fromCallable( {charactersDB?.run{
            CharactersDAO().deleteAll()
        }})
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                disposable.add(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Unit>(){
                override fun onNext(t: Unit) {

                }

                override fun onComplete() {
                    Log.d("deleteAllRows()","Delete Charecters DB Completed")
                }

                override fun onError(e: Throwable) {
                    Log.d("deleteAllRows()","Error = ${e.message}")
                }

            })

    }

    fun updateDBSImages(){
        for (i in dbCharacters){
            LoadImageToDBS(getApplication<Application>().applicationContext,i,this)
        }

    }


    fun updateCharacter(character : CharecterDBEntity){
        Observable.fromCallable( {charactersDB?.CharactersDAO()?.updateCharecters(character)})
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                disposable.add(it)
            }
            .subscribe(object : DisposableObserver<Unit>(){
                override fun onNext(t: Unit) {

                }

                override fun onComplete() {
                    Log.d("UpdateCharacter()","Update Charecters DB Completed")
                }

                override fun onError(e: Throwable) {
                    Log.d("UpdateCharacter()","Error = ${e.message}")
                }

            })
    }
    // update DBS if we are online
    fun updateDBS(value : CharacterList){

        dbCharacters.clear()

        // ROOM and Retrofit have different data structures to hold the DATA
        for (i in 0..value.characterList.size-1)
        {
            dbCharacters.add(CharecterDBEntity(id=i,name=value.characterList[i].name.toString(),
                gender=value.characterList[i].gender.toString(),url=value.characterList[i].image.toString(),imageRawData = "EMPTY"))

//            characterList.add(CharecterDBEntity(id=i,name=value.characterList[i].name.toString(),
//                gender=value.characterList[i].gender.toString(),url=value.characterList[i].image.toString(),imageRawData = "EMPTY"))
        }

        Observable.fromCallable( {charactersDB?.CharactersDAO()?.insertAll(dbCharacters)})
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                disposable.add(it)
            }
            .subscribe(object : DisposableObserver<Unit>(){
                override fun onNext(t: Unit) {

                }

                override fun onComplete() {
                    Log.d("updateDBS()","Update Charecters DB Completed")
                }

                override fun onError(e: Throwable) {
                    Log.d("updateDBS()","Error = ${e.message}")
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
                    override fun onSuccess(value: CharacterList) {
                        characters.value?.let{
                            var characterListTemp = CharacterList()
                            characterListTemp.characterList = mutableListOf<Character>()
                            characterListTemp.info = Info(value.info.next)
                            characterListTemp.characterList.addAll(it.characterList)
                            characterListTemp.characterList.addAll(value.characterList)
                            // update all characters to the new big list
                            characters.value = characterListTemp
                        }
                        deleteAllRows()
                        updateDBS(characters.value!!)
                        updateDBSImages()
                        countryLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        countryLoadError.value = true
                        loading.value = false
                    }
                }
                )

        )
    }
}

fun MutableList<Character>.copyDBList(dblist : List<CharecterDBEntity>) {

    for (i in 0..dblist.size-1)
    {
        var character = Character(dblist[i].name,dblist[i].gender,dblist[i].url,i,dblist[i].imageRawData)
        this.add(character)
    }
}