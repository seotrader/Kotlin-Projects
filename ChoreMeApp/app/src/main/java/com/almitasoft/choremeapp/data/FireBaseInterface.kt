package com.almitasoft.choremeapp.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.almitasoft.choremeapp.model.*
import io.reactivex.Observable

interface FireBaseInterface {
    fun uploadUserImage(uri : Uri, byteArray: ByteArray) : Observable<Result>
    fun deleteFriend(user : User) : Observable<Result>
    fun deleteNotification(notification: Notification) : Observable<Result>
    fun getNotifications2() : Observable<ArrayList<AddFriendNotification>>
    fun getFriendsList() : Observable<ArrayList<User>>
    fun deleteFriendRequest(notification: AddFriendNotification) : Observable<Result>
    fun getTargetUserData(userID: String) : LiveData<User>
    fun getTargetUserData2(userID: String) : Observable<User>
    fun addToFriends(user : User) : LiveData<Result>
    fun deleteBroadCastotifications() : LiveData<Result>
    fun getBroadCaseNotifications() : LiveData<ArrayList<Notification>>
    fun deleteFriendNotification(notification: AddFriendNotification) : Observable<Result>
    fun deleteGeneralNotification(notification: GeneralNotification) : Observable<Result>
    fun addFriendNotification(notification : AddFriendNotification) : LiveData<Result>
    fun getCurrentUserData() : LiveData<Result>
    fun addGeneralNotification(notification : GeneralNotification) : Observable<Result>
    fun addGeneralPushNotification(notification : GeneralNotification) : Observable<Result>
    fun getGeneralNotifications() : Observable<ArrayList<GeneralNotification>>
    fun addFriendRequest(user : User) : LiveData<Result>
    fun getNotifications() : LiveData<ArrayList<AddFriendNotification>>
}