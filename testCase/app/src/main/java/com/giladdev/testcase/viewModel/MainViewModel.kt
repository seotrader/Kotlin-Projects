package com.giladdev.testcase.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.giladdev.testcase.model.BuildingApiService
import com.giladdev.testcase.model.BuildingInfo
import com.giladdev.testcase.model.PurchaseInfo
import com.giladdev.testcase.model.PurchasesApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject

class MainViewModel(app : Application): AndroidViewModel(app){
    var volleyRequest : RequestQueue? = null
    var purchasesList = MutableLiveData<MutableList<PurchaseInfo>>()
    var buildingList = MutableLiveData<MutableList<BuildingInfo>>()
    var tempList = arrayListOf<PurchaseInfo>()

    // for RX Java
    private val disposable = CompositeDisposable()

    // JSON urls to read te DATA
    val purchaseUrl = "http://positioning-test.mapsted.com/api/Values/GetAnalyticData"
    val buildingUrl = "http://positioning-test.mapsted.com/api/Values/GetBuildingData"



    init{
        volleyRequest = Volley.newRequestQueue(app)
    }

    // Use Retrofit + RX JAVA to read the JSON buildings
    fun getBuildingsRxJava(){
        var purchaseService = BuildingApiService()

        disposable.add(
            purchaseService.getBuildings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<BuildingInfo>>() {
                    override fun onSuccess(value: MutableList<BuildingInfo>?) {
                        buildingList.value = value
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("getPurchasesRxJava Error",e?.message.toString())
                    }
                }
                )
        )
    }

    // Use Retrofit + RX JAVA to read the purchases
    fun getPurchasesRxJava(){
        var purchaseService = PurchasesApiService()

        disposable.add(
            purchaseService.getPurchases()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<PurchaseInfo>>() {
                    override fun onSuccess(value: MutableList<PurchaseInfo>?) {
                        purchasesList.value = value
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("getPurchasesRxJava Error",e?.message.toString())
                    }
                }
                )
        )
    }

    fun getBuildings(){

    }

}