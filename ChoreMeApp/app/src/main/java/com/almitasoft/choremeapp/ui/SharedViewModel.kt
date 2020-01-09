package com.almitasoft.choremeapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SharedViewModel : ViewModel() {
    var updatedFilteredText = MutableLiveData<String>()

    val mainActivity = MutableLiveData<MainActivity>().apply {
        value = null
    }

    fun isUserConnected() = (FirebaseAuth.getInstance().currentUser != null)
}

