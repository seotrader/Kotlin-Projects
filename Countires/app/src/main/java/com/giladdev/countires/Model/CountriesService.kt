package com.giladdev.countires.Model

import com.giladdev.countires.di.DaggerApiComponenet
import io.reactivex.Single
import retrofit2.Retrofit
import javax.inject.Inject

class CountriesService {

    @Inject
    lateinit var api: CountriesApi

    init {
        DaggerApiComponenet.create().inject(this)
    }

    fun getCountires() : Single<List<Country>>
    {
        return api.getCountires()
    }

}