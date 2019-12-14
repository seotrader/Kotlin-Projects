package com.giladdev.testcase.model

import io.reactivex.Single
import retrofit2.http.GET

interface Api {
    @GET("GetAnalyticData/")
    fun getPurchases() : Single<MutableList<PurchaseInfo>>

    @GET("GetBuildingData/")
    fun getBuildings() : Single<MutableList<BuildingInfo>>
}