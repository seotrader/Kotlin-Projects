package com.giladdev.rickyandmarty.model
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CharectersApiService{
    private val BASE_URL = "https://rickandmortyapi.com/api/"
    private var api: Api
    var pageNumber : String? ="1"

    init {
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(Api::class.java)

    }

    fun getCountires() : Single<CharacterList>
    {
        return api.getCharecters(pageNumber)
    }
}

