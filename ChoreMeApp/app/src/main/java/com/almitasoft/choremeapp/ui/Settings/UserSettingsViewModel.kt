package com.almitasoft.choremeapp.ui.Settings

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.almitasoft.choremeapp.model.Result
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseInterface
import io.reactivex.disposables.CompositeDisposable

class UserSettingsViewModel(var fb : FireBaseInterface) : ViewModel() {

    private val disposable = CompositeDisposable()
    var uploadCompleted = MutableLiveData<Result>()

    fun getCurrentUser() : LiveData<Result>{
        return fb.getCurrentUserData()
    }

    fun uploadUserProfile(uri: Uri,byteArray: ByteArray) : LiveData<Result>{
        disposable.add(fb.uploadUserImage(uri,byteArray).subscribe{
            uploadCompleted.value = it
        })
        return uploadCompleted
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}
