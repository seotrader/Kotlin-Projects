package com.example.recyclerviewexample.model

import io.reactivex.rxjava3.core.Single

interface WeatherNetworkClient {
    fun getData(): Single<List<City>>
}

class WeatherNetworkClientImpl : WeatherNetworkClient {
    override fun getData(): Single<List<City>> {
        val citiesList = arrayListOf<City> (
            City("Israel", "Tel Aviv"),
            City("Canada", "Toronto"),
            City("Argentina", "Mendoza"),
            City("USA", "Miami"),
            City("Israel", "Ashdod"),
            City("Argentina", "Buenos Aires"),
            City("USA", "California"),
            City("Canada", "Calgary"),
            City("USA", "Detroit")
        )
        return Single.just(citiesList)
    }
}