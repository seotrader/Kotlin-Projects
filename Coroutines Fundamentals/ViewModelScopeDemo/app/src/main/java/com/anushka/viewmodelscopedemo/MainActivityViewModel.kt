package com.anushka.viewmodelscopedemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


class MainActivityViewModel : ViewModel() {
    private val userRepo = UserRepository()
    var users = MutableLiveData<List<User>>()
    
 fun getUserData(){
     viewModelScope.launch {
         //write some code
         var result: List<User>? = null
         withContext(Dispatchers.IO) {
             result = userRepo.getUsers()
         }
         users.value = result
     }
     
     
 }

    override fun onCleared() {
        super.onCleared()
    }

}