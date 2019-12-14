package com.giladdev.testcase.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PurchasesApiService {
    private val BASE_URL = "http://positioning-test.mapsted.com/api/Values/"
    private var api: Api

    init {
        // return RX Java observable with Retrofit
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(Api::class.java)

    }

    fun getPurchases() : Single<MutableList<PurchaseInfo>>
    {
        return api.getPurchases()
    }
}