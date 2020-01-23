package com.almitasoft.choremeapp.ui.notifications


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseInterface
import com.almitasoft.choremeapp.model.AddFriendNotification
import com.almitasoft.choremeapp.model.GetUserDataResult
import com.almitasoft.choremeapp.model.Notification
import com.almitasoft.choremeapp.model.User
import io.reactivex.disposables.CompositeDisposable

class NotificationsViewModel(var fb : FireBaseInterface) : ViewModel() {

    val notificationList = MutableLiveData<ArrayList<Notification>>()
    var targetUserData = MutableLiveData<User>()

    private val deleteFriendStatus = MutableLiveData<GetUserDataResult>()

    private val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun getTargetUserData(user : User) : LiveData<User>{
        disposable.add(
            fb.getTargetUserData2(user.userID).subscribe{
                targetUserData.value = it
            }
        )
        return targetUserData
    }

    fun deleteFriendNotification(notification : AddFriendNotification) : LiveData<GetUserDataResult>{
        disposable.add(
            fb.deleteFriendNotification(notification).subscribe({
                deleteFriendStatus.value = it as GetUserDataResult
            } ,{
                Log.d("Error.deleteFriendNotification",it.message.toString())
            })
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
