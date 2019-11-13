package com.giladdev.countires.Model

import io.reactivex.Single
import retrofit2.http.GET

interface CountriesApi {
    @GET("  DevTides/countries/master/countriesV2.json")
    fun getCountires():Single<List<Country>>

}