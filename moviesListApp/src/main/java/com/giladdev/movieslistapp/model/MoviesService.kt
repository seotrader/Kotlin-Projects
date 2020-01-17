package com.giladdev.movieslistapp.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MoviesService
{
    private val BASE_URL = "https://movies-sample.herokuapp.com/"
    private var api: MoviesApi
    init {
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MoviesApi::class.java)

    }

    fun getMovies() : Single<MoviesObject>
    {
        return api.getMovies()
    }
}