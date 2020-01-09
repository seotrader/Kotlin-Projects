package com.almitasoft.choremeapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.almitasoft.choremeapp.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class FireBaseManager {
    lateinit var mDataBase : DatabaseReference
    var mCurrentUser : FirebaseUser?=null
    var addFriendRequestResult = MutableLiveData<Result>()
    var notificationsList = MutableLiveData<ArrayList<AddFriendNotification>>()
    var addNotificationResult = MutableLiveData<Result>()

    var currentUserDataResult = MutableLiveData<Result>()

    fun addFriendNotification(notification : AddFriendNotification) : LiveData<Result>
    {
        var userObj = hashMapOf<String,String>()

        mDataBase = FirebaseDatabase.getInstance().reference.child("Notifications").
            child(CurrentUser.userID!!).push()

        userObj.put("UID", notification.sourceUID)
        userObj.put("Source_UName", notification.sourceUName)
        userObj.put("Target_UID", notification.targetUID)
        userObj.put("Target_UName",notification.targetUName)
        userObj.put("Message",notification.notificationMessage)
        userObj.put("Status",notification.status)

        var addUserResult = AddFriendResult("OK")

        mDataBase.setValue(userObj).addOnCompleteListener {task->
            if (task.isSuccessful) {
                addUserResult.result = "OK"
                addFriendRequestResult.value = addUserResult
            }
            else{
                addUserResult.result = "ERROR = ${task.result.toString()}"

                addFriendRequestResult.value = addUserResult
            }
        }

        return addFriendRequestResult

    }
    fun getCurrentUserData() : LiveData<Result>{
        mCurrentUser =  FirebaseAuth.getInstance().currentUser
        var result = GetUserDataResult("OK")

        mDataBase = FirebaseDatabase.getInstance().reference.child("Users")
            .child(mCurrentUser!!.uid)

        mDataBase.addValueEventListener( object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                result.result = p0.message
                currentUserDataResult.value = result

            }

            override fun onDataChange(p0: DataSnapshot) {
                CurrentUser.userID = p0.key
                CurrentUser.displanyName = p0.child("display name").value.toString()
                CurrentUser.userEmail = p0.child("email").value.toString()
                CurrentUser.status = p0.child("status").value.toString()
                CurrentUser.image_url = p0.child("image").value.toString()
                CurrentUser.thumb_image_url = p0.child("thumb_image").value.toString()

                result.result = "OK"

                currentUserDataResult.value = result

            }
        })
        return currentUserDataResult
    }

    fun addFriendRequest(user : User) : LiveData<Result>{

        var userObj = hashMapOf<String,String>()

        mDataBase = FirebaseDatabase.getInstance().reference.child("FriendsRequests").
            child(CurrentUser.userID!!).child(user.userID)

        var status = FriendsStatus.WAITING.toString()

        userObj.put("Source_UID", CurrentUser.userID!!)
        userObj.put("Source_UName", CurrentUser.displanyName!!)
        userObj.put("Target_UID", user.userID)
        userObj.put("Target_UName", user.displayName)
        userObj.put("status",status)

        var addUserResult = AddFriendResult("OK")

        mDataBase.setValue(userObj).addOnCompleteListener {task->
            if (task.isSuccessful) {
                var ref = FirebaseDatabase.getInstance().reference.child("FriendsRequests")
                    .child(user.userID).child(CurrentUser.userID!!)

                ref.setValue(userObj).addOnCompleteListener {task2 ->
                    if (task2.isSuccessful) {
                        addUserResult.result = "OK"
                        addFriendRequestResult.value = addUserResult
                    }
                    else{
                        addUserResult.result = "ERROR = ${task.result.toString()}"
                    }
                }
            }
            else{
                addUserResult.result = "ERROR = ${task.result.toString()}"

                addFriendRequestResult.value = addUserResult
            }
        }

        return addFriendRequestResult
    }

    fun getNotifications() : LiveData<ArrayList<AddFriendNotification>>{
        mCurrentUser =  FirebaseAuth.getInstance().currentUser

        mDataBase = FirebaseDatabase.getInstance().reference.child("FriendsRequests")
            .child(mCurrentUser!!.uid)

        mDataBase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error getNotifications()","Error = ${p0.message}")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var tempNotificationList = arrayListOf<AddFriendNotification>()

                p0.children.forEach {snapshot->
                    var sourceUID = snapshot.child("Source_UID").value.toString()
                    var sourceUname = snapshot.child("Source_UName").value.toString()
                    var targetUID = snapshot.child("Target_UID").value.toString()
                    var status = snapshot.child("status").value.toString()

                    if (targetUID == CurrentUser.userID){
                        var newFriendNotification = AddFriendNotification("New Friend Request From: ${sourceUname}")
                        newFriendNotification.sourceUID = sourceUID
                        newFriendNotification.status = status
                        newFriendNotification.targetUID = targetUID

                        tempNotificationList.add(newFriendNotification)

                        notificationsList.value = tempNotificationList


                    }
                }
            }
        })

        return notificationsList
    }

}