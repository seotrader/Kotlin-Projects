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
                            Log.d("ViewModel","Read successfully the cities. cities = $it")
                            cities.value = sortAndGroup(it)
                        },
                        // On error
                        {
                            Log.e("Error", "Error = ${it.message}")
                        })
    }

    private fun sortAndGroup(cities: List<City>): List<City> {
        // Sort and group
        val sortedHash = cities.sortedBy {
            it.city
        }.groupBy {
            it.country
        }.toSortedMap()

        val sortedGroupedList = arrayListOf<City>()
        sortedHash.keys.forEach {country->
            sortedGroupedList.add(City(country, ""))
            sortedHash[country]?.let {
                sortedGroupedList.addAll(it.map {city -> City(country, city.city)})
            }
        }

        Log.d("Main", "cities = $sortedGroupedList")
        return sortedGroupedList
    }
}