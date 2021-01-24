package com.example.recyclerviewexample.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerviewexample.model.City
import com.example.recyclerviewexample.model.WeatherNetworkClientImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val cities: MutableLiveData<List<City>> by lazy {
        MutableLiveData<List<City>>().also {
            loadCountries()
        }
    }

    fun getCountries(): LiveData<List<City>> {
        return cities
    }

    private val weatherNetworkClient = WeatherNetworkClientImpl()

    private fun loadCountries() {
        weatherNetworkClient.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // On success
                        {
                            Log.d("ViewModel","Read succesfully the cities. cities = $it")
                            val sortedList = sortAndGroup(it)
                            cities.value = sortedList
                        },
                        // On error
                        {
                            Log.d("Error", "Error = ${it.message}")
                        })
    }

    private fun sortAndGroup(cities: List<City>): List<City> {
        // Sort and group
        var sortedHash = cities.sortedBy {
            it.city
        }.groupBy {
            it.country
        }.toSortedMap()

        var sortedGroupedList = arrayListOf<City>()
        sortedHash.keys.forEach {
            sortedGroupedList.add(City(it, ""))
            sortedHash[it]?.forEach {city->
                sortedGroupedList.add(City(it, city.city))
            }
        }

        Log.d("Main", "cities = $sortedGroupedList")
        return sortedGroupedList
    }
}