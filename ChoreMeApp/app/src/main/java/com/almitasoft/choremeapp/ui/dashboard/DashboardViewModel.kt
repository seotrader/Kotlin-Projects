package com.almitasoft.choremeapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.model.User
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

class DashboardViewModel : ViewModel(), KoinComponent {

    private val disposable = CompositeDisposable()
    private val fb : FireBaseManager by inject()

    private val _text = MutableLiveData<String>().apply {
        value = "Here You Will See All Your Tasks..."
    }
    val text: LiveData<String> = _text
}