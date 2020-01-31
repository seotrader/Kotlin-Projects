package com.almitasoft.choremeapp.ui.notifications


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseInterface
import com.almitasoft.choremeapp.model.*
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

    fun removeGeneralNotification(notification: GeneralNotification) : LiveData<Result>{
        val addNotificationResult = MutableLiveData<Result>()

        disposable.add(
            fb.deleteGeneralNotification(notification).subscribe( { result ->
                result.let {
                    addNotificationResult.value = it
                }
            }, {
                Log.d("Error",
                    "NotificationsViewModel.addGeneralNotification. error = ${it.message}")
            })
        )
        return addNotificationResult
    }

    fun addPushNotification(notification: GeneralNotification) : LiveData<Result>{

        val addNotificationResult = MutableLiveData<Result>()

        disposable.add(
            fb.addGeneralPushNotification(notification).subscribe( { result ->
                result.let {
                    addNotificationResult.value = it
                }
            }, {
                Log.d("Error",
                    "Pushnotification.addGeneralNotification. error = ${it.message}")
            })
        )
        return addNotificationResult
    }

    fun addGeneralNotification(notification: GeneralNotification) : LiveData<Result>{

        val addNotificationResult = MutableLiveData<Result>()

        disposable.add(
            fb.addGeneralNotification(notification).subscribe( { result ->
                result.let {
                    addNotificationResult.value = it
                }
            }, {
                Log.d("Error",
                    "NotificationsViewModel.addGeneralNotification. error = ${it.message}")
            })
        )
        return addNotificationResult
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

        val deleteFriendResult = MutableLiveData<GetUserDataResult>()

        disposable.add(
            fb.deleteFriendNotification(notification).subscribe({
                deleteFriendResult.value = it as GetUserDataResult
            } ,{
                Log.d("Error.deleteFriendNotification",it.message.toString())
            })
        )

        return deleteFriendResult
    }

    fun refreshData()
    {
        disposable.add(
            fb.getNotifications2().subscribe {addfriendsNotifications->
                val addFriendNotificationList = ArrayList<Notification>()
                addFriendNotificationList.addAll(addfriendsNotifications)

                fb.getGeneralNotifications().subscribe(
                    {generalNotifications->
                        val tempArray = ArrayList<Notification>()

                        if (addFriendNotificationList.isNotEmpty()){
                            tempArray.addAll(addFriendNotificationList)
                        }

                        tempArray.addAll(generalNotifications)
                        notificationList.value = tempArray
                        Log.d("Info","NotificationViewModel::refreshData - " +
                                "Added all notifications")
                    },{
                        Log.d("Error","NotificationViewModel::refreshData -"+
                                "Error = ${it.message}"
                        )
                    }
                )
            }
        )
    }
}
