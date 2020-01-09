package com.almitasoft.choremeapp.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.model.AddFriendNotification
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotificationsViewModel : ViewModel() , KoinComponent{

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    private val fb : FireBaseManager by inject()

    fun getUsersList() : LiveData<ArrayList<AddFriendNotification>> {
        return fb.getNotifications()
    }
}