package com.giladdev.rickyandmarty.model

import retrofit2.http.GET
import retrofit2.Call


interface Api {
    @GET("character")
    fun GetCharecters() : Call<CharacterList>
    }
