package com.giladdev.countires.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.giladdev.countires.Model.CountriesService
import com.giladdev.countires.Model.Country
import com.giladdev.countires.di.DaggerApiComponenet
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel : ViewModel() {
    @Inject
    lateinit var countiresservice : CountriesService

    init {
        DaggerApiComponenet.create().inject(this)
    }
    val countires = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()


    fun refresh() {
        FetchCountires()
    }

    private fun FetchCountires() {
        loading.value = true // loading is starting
        disposable.add(
            countiresservice.getCountires()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                        override fun onSuccess(value: List<Country>?) {
                            countires.value = value
                            countryLoadError.value = false
                            loading.value = false
                        }

                        override fun onError(e: Throwable?) {
                            countryLoadError.value = true
                            loading.value = false
                        }

                    }
                    )

        )


    }
}