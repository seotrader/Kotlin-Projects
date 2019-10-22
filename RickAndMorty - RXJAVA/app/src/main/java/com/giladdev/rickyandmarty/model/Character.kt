package com.giladdev.rickyandmarty.model
import com.google.gson.annotations.SerializedName

class CharacterList{
    @SerializedName ("info")
    lateinit var info : Info
    @SerializedName("results")
    lateinit var characterList : MutableList<Character>
}

data class Info(
    var next:String?)

data class Character(
    var name:String?,
    var gender:String?,
    var image:String?)


