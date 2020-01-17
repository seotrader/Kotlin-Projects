package com.almitasoft.choremeapp.data

import androidx.lifecycle.LiveData
import com.almitasoft.choremeapp.model.AddFriendNotification
import com.almitasoft.choremeapp.model.Notification
import com.almitasoft.choremeapp.model.Result
import com.almitasoft.choremeapp.model.User
import io.reactivex.Observable

interface FireBaseInterface {
    fun deleteFriend(user : User) : Observable<Result>
    fun deleteNotification(notification: Notification) : Observable<Result>
    fun getNotifications2() : Observable<ArrayList<AddFriendNotification>>
    fun getFriendsList() : Observable<ArrayList<User>>
    fun deleteFriendRequest(notification: AddFriendNotification) : Observable<Result>
    fun getTargetUserData(userID: String) : LiveData<User>
    fun addToFriends(user : User) : LiveData<Result>
    fun deleteBroadCastotifications() : LiveData<Result>
    fun getBroadCaseNotifications() : LiveData<ArrayList<Notification>>
    fun addFriendNotification(notification : AddFriendNotification) : LiveData<Result>
    fun getCurrentUserData() : LiveData<Result>
    fun addFriendRequest(user : User) : LiveData<Result>
    fun getNotifications() : LiveData<ArrayList<AddFriendNotification>>
}