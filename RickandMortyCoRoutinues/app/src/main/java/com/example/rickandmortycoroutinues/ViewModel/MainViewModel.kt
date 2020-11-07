package com.example.rickandmortycoroutinues.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rickandmortycoroutinues.Data.Character
import com.example.rickandmortycoroutinues.Data.CharacterList

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val characters = MutableLiveData<CharacterList>()

    fun generateDummyList() {
        var list = CharacterList()
        var characterList = arrayListOf<Character>()

        characterList.add(Character("Leo messi","male",null, null,null))
        characterList.add(Character("Christiano Ronaldo","male",null, null,null))
        characterList.add(Character("Louis Suarez","male",null, null,null))
        list.characterList = characterList
        characters.value = list
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MainViewModel","View model cleared")
    }
}