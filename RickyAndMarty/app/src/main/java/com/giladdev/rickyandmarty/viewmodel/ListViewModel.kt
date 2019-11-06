package com.giladdev.rickyandmarty.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.giladdev.rickyandmarty.model.CharacterList
import com.giladdev.rickyandmarty.model.CharectersApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.net.URL

class ListViewModel(): ViewModel() {

    var charecterService = CharectersApiService()
    val characters = MutableLiveData<CharacterList>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()

    fun refresh(){
        val nextUrl : String?= characters.value?.info?.next

        if (nextUrl==null) charecterService.pageNumber="1"
        else {

            var urlParameters = Uri.parse(characters.value?.info?.next)
            var pageNum = urlParameters.getQueryParameter("page")
            charecterService.pageNumber = pageNum?:"1"
        }

        fetchCharacters()
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
                        characters.value = value
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

