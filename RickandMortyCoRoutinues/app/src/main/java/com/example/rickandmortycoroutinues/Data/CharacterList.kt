package com.example.rickandmortycoroutinues.Data

class CharacterList{
    lateinit var info : Info
    lateinit var characterList : MutableList<Character>
}

data class Info(
    var next:String?)

data class Character(
    var name:String?,
    var gender:String?,
    var image:String?,
    var id:Int?,
    var imageRawData:String?)