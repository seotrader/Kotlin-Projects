package com.giladdev.rickyandmarty.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

class CharacterList{
    @SerializedName("results")
    var characterList : List<Character>?=null
}

data class Character(
    var name:String?=null,
    var gender:String?=null,
    var image:String?=null)


