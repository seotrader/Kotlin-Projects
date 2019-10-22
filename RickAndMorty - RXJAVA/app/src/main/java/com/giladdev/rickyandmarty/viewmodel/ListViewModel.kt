package com.giladdev.rickyandmarty.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.giladdev.rickyandmarty.model.Character
import com.giladdev.rickyandmarty.model.CharacterList
import com.giladdev.rickyandmarty.model.CharectersApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel: ViewModel() {
    var charecterService : CharectersApiService

    val characters = MutableLiveData<CharacterList>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()


    init{
        charecterService = CharectersApiService()
    }

    fun refresh(){
        charecterService.pageNumber = characters.value?.info?.next?.substringAfter("?page=","1")
        FetchCharacters()
    }

    private fun FetchCharacters() {
        loading.value = true // loading of data starting

        // call the API via RX java and observe
        disposable.add(
            charecterService.getCountires()
                .subscribeOn(Schedulers.newThread())
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

