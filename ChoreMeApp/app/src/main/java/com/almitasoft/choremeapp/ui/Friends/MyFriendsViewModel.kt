package com.almitasoft.choremeapp.ui.Friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyFriendsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is friends fragement "
    }
    val text: LiveData<String> = _text
}