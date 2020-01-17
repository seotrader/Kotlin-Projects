package com.giladdev.movieslistapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.giladdev.movieslistapp.model.Movie
import com.giladdev.movieslistapp.model.MoviesObject
import com.giladdev.movieslistapp.model.MoviesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel(){

    // live data of the movies list
    val moviesList = MutableLiveData<MutableList<Movie>>()

    // indicates when we are loading movies
    val moviesLoading  = MutableLiveData<Boolean>()

    // when we have an error
    val moviesError  = MutableLiveData<String>()

    var moviesService = MoviesService()

    // for RX Java
    private val disposable = CompositeDisposable()

    fun refresh(){
        getMovies()
    }

    fun getMovies(){
        moviesLoading.value = true
        disposable.add(
            moviesService.getMovies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<MoviesObject>(){
                    override fun onSuccess(value: MoviesObject?) {
                        moviesError.value = "OK"
                        moviesLoading.value = false
                        moviesList.value = value!!.moviesList?.toMutableList()
                    }

                    override fun onError(e: Throwable?) {
                        moviesError.value = e?.message
                        moviesLoading.value = false
                    }
                })
        )
    }

}