package com.almitasoft.choremeapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.model.Notification
import com.almitasoft.choremeapp.model.Result
import com.almitasoft.choremeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

class SharedViewModel : ViewModel(), KoinComponent {

    // holds current user friends list
    var friendsList = MutableLiveData<ArrayList<User>>()

    var updatedFilteredText = MutableLiveData<String>()
    private val disposable = CompositeDisposable()

    private val fb : FireBaseManager by inject()

    val mainActivity = MutableLiveData<MainActivity>().apply {
        value = null
    }


    fun getBroadCastNotifications() : LiveData<ArrayList<Notification>>{
        return fb.getBroadCaseNotifications()
    }

    fun deleteBroadCastNotification() : LiveData<Result>{
        return fb.deleteBroadCastotifications()
    }

    fun getTargetUserData(user : User) : LiveData<User>
    {
        return fb.getTargetUserData(user.userID)
    }

    fun addFriend(user : User) : LiveData<Result>{
        return fb.addToFriends(user)
    }

    fun isUserConnected() = (FirebaseAuth.getInstance().currentUser != null)
}

