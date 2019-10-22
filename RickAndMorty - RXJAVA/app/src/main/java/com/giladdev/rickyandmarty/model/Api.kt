package com.giladdev.rickyandmarty.model

import io.reactivex.Single
import retrofit2.http.*

interface Api {
    @GET("character/")
    fun GetCharecters(@Query("page") page : String?) : Single<CharacterList>
    }
