package com.almitasoft.choremeapp.ui.Friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.model.Result
import com.almitasoft.choremeapp.model.User
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.Koin
import org.koin.core.KoinComponent
import org.koin.core.inject

class MyFriendsViewModel : ViewModel(),KoinComponent {

    private val fb : FireBaseManager by inject()


    private val _text = MutableLiveData<String>().apply {
        value = "This is friends fragement "
    }
    val text: LiveData<String> = _text




}