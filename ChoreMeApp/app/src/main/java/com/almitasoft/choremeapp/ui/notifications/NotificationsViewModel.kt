package com.almitasoft.choremeapp.ui.notifications


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseInterface
import com.almitasoft.choremeapp.model.GetUserDataResult
import com.almitasoft.choremeapp.model.Notification
import com.almitasoft.choremeapp.model.User
import io.reactivex.disposables.CompositeDisposable

class NotificationsViewModel(var fb : FireBaseInterface) : ViewModel() {

    val notificationList = MutableLiveData<ArrayList<Notification>>()
    private val deleteFriendStatus = MutableLiveData<GetUserDataResult>()

    private val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun deleteFriend(user : User) : LiveData<GetUserDataResult>{
        disposable.add(
            fb.deleteFriend(user).subscribe{
                deleteFriendStatus.value = it as GetUserDataResult
            }
        )
        return deleteFriendStatus
    }

    fun refreshData()
    {
        disposable.add(
            fb.getNotifications2().subscribe {
                val tempArray = ArrayList<Notification>()
                tempArray.addAll(it)
                notificationList.value = tempArray}
        )
    }
}
