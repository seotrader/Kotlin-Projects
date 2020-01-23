package com.almitasoft.choremeapp.data

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.almitasoft.choremeapp.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import id.zelory.compressor.Compressor
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File

class FireBaseManager : FireBaseInterface {

    lateinit var mDataBase : DatabaseReference
    var mCurrentUser : FirebaseUser?=null
    var addFriendRequestResult = MutableLiveData<Result>()
    var notificationsList = MutableLiveData<ArrayList<AddFriendNotification>>()
    var currentUserDataResult = MutableLiveData<Result>()
    var broadCastNotificationList = MutableLiveData<ArrayList<Notification>>()
    var deletenotificationsResult = MutableLiveData<Result>()
    var addFriendResult = MutableLiveData<Result>()
    var requestedTargetUser = MutableLiveData<User>()
    var requestDeleteFriend = MutableLiveData<Result>()

    var mStorageRef = FirebaseStorage.getInstance().reference
    var userID = FirebaseAuth.getInstance().currentUser

    override fun getTargetUserData2(userID: String): Observable<User> {
        var targetUser : User?=null

        return Observable.create(ObservableOnSubscribe<User> {emitter->
            mCurrentUser =  FirebaseAuth.getInstance().currentUser

            mDataBase = FirebaseDatabase.getInstance().reference.child("Users")
                .child(userID)

            mDataBase.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("Error getTargetUserData2()","Error = ${p0.message}")
                    emitter.onError(Throwable(p0.message))
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val displayName = p0.child("display name").value.toString()
                    val userEmail = p0.child("email").value.toString()
                    val status = p0.child("status").value.toString()
                    val image_url = p0.child("image").value.toString()
                    val thumb_image_url = p0.child("thumb_image").value.toString()

                    var user = User(displayName, userID)
                    user.userEmail = userEmail
                    user.status = status
                    user.image_url = image_url
                    user.thumb_image_url = thumb_image_url
                    emitter.onNext(user)
                    emitter.onComplete()
                }
            })

        })
    }

    override fun uploadUserImage(uri : Uri, byteArray: ByteArray) : Observable<Result>{

        var thumbFile = File(uri.path.toString())

        return Observable.create(ObservableOnSubscribe<Result> { emitter ->
            var result = GetUserDataResult("OK")

            var filePath = mStorageRef.child("chat_profile_images")
                .child(userID.toString()+".jpg")

            if (userID == null){
                emitter.onError(Throwable("ERROR: User isn't connected"))
            }else{
                // create another directory for thumbImages ( smallaer compressed images)
                var thumbFilePath = mStorageRef.child("chat_profile_images")
                    .child("thumbs  ")
                    .child(userID.toString()+".jpg")

                filePath.putFile(uri)
                    .addOnCompleteListener {
                        var downloadUrl : String?= null
                        if (it.isSuccessful) {
                            filePath.downloadUrl.addOnCompleteListener {task->
                                if (task.isSuccessful){
                                    downloadUrl = task.result.toString()
                                }
                                else{
                                    emitter.onError(Throwable(task.exception.toString()))
                                }
                            }
                        }

                        // upload task
                        var uploadTask : UploadTask = thumbFilePath
                            .putBytes(byteArray)
                        uploadTask.addOnCompleteListener {
                            var thumbUrl : String
                            // it.result!!.storage.downloadUrl.toString()
                            if (it.isSuccessful){
                                it.result!!.storage.downloadUrl.addOnCompleteListener {
                                    thumbUrl = it.result.toString()
                                    var updateObj = HashMap<String, Any>()
                                    updateObj.put("image",downloadUrl.toString())
                                    updateObj.put("thumb_image",thumbUrl.toString())
                                    // save the profile image
                                    mDataBase.updateChildren(updateObj)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful){
                                                emitter.onNext(result)
                                                emitter.onComplete()
                                            }else{
                                                result.result = it.exception.toString()
                                                emitter.onNext(result)
                                                emitter.onComplete()
                                            }

                                        }
                                }
                            }else{
                            }

                        }
                    }
            }
        })
    }
    override fun deleteFriendNotification(notification: AddFriendNotification): Observable<Result> {
        return Observable.create(ObservableOnSubscribe<Result>{emitter->
            var result = GetUserDataResult("OK")

            mCurrentUser =  FirebaseAuth.getInstance().currentUser

            mDataBase = FirebaseDatabase.getInstance().reference.child("FriendsRequests")
                .child(mCurrentUser!!.uid).child(notification.sourceUID)

            mDataBase.removeValue().addOnCompleteListener {task->
                if (task.isSuccessful){
                    emitter.onNext(result)
                }
                else{
                    Log.d("Error deleteFriendNotification()","Error = ${task.result.toString()}")
                    emitter.onError(Throwable(task.result.toString()))
                }
            }
        }
        )
    }

    override fun deleteNotification(notification: Notification): Observable<Result> {
        return Observable.create(ObservableOnSubscribe<Result>{emitter->
            var result = GetUserDataResult("OK")

            mCurrentUser =  FirebaseAuth.getInstance().currentUser

            mDataBase = FirebaseDatabase.getInstance().reference.child("Friends")
                .child(mCurrentUser!!.uid).child(notification.notificationID)

            mDataBase.removeValue().addOnCompleteListener {task->
                if (task.isSuccessful){
                    emitter.onNext(result)
                }
                else{
                    Log.d("Error getNotifications()","Error = ${task.result.toString()}")
                    emitter.onError(Throwable(task.result.toString()))
                }
            }
        }
        )

    }

    override fun getTargetUserData(userID: String) : LiveData<User>{

        mCurrentUser =  FirebaseAuth.getInstance().currentUser
        var result = GetUserDataResult("OK")

        mDataBase = FirebaseDatabase.getInstance().reference.child("Users")
            .child(userID)

        mDataBase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                requestedTargetUser.value = null
            }

            override fun onDataChange(p0: DataSnapshot) {
                val displayName = p0.child("display name").value.toString()
                val userEmail = p0.child("email").value.toString()
                val status = p0.child("status").value.toString()
                val image_url = p0.child("image").value.toString()
                val thumb_image_url = p0.child("thumb_image").value.toString()

                var user = User(displayName, userID)
                user.userEmail = userEmail
                user.status = status
                user.image_url = image_url
                user.thumb_image_url = thumb_image_url
                requestedTargetUser.value = user
            }
        })
        return requestedTargetUser
    }


    override fun addToFriends(user : User) : LiveData<Result>{
        var userObj = hashMapOf<String,String>()

        mDataBase = FirebaseDatabase.getInstance().reference.child("Friends").
            child(CurrentUser.userID!!).child(user.userID)

        var addUserResult = AddFriendResult("OK")

        userObj.put("userid", user.userID)
        userObj.put("displayname", user.displayName)
        userObj.put("imageurl", user.image_url!!)
        userObj.put("thumbimageurl", user.thumb_image_url!!)
        userObj.put("status", user.status!!)


        mDataBase.setValue(userObj).addOnCompleteListener {task->
            if (task.isSuccessful) {
                mDataBase = FirebaseDatabase.getInstance().reference.child("Friends").
                    child(user.userID).child(CurrentUser.userID!!)

                userObj.clear()

                userObj.put("userid", CurrentUser.userID!!)
                userObj.put("displayname", CurrentUser.displanyName!!)
                userObj.put("imageurl", CurrentUser.image_url!!)
                userObj.put("thumbimageurl", CurrentUser.thumb_image_url!!)
                userObj.put("status", CurrentUser.status!!)

                mDataBase.setValue(userObj).addOnCompleteListener { task2 ->
                    if (task2.isSuccessful){
                        addUserResult.result = "OK"
                        addFriendResult.value = addUserResult
                    }
                    else{
                        addUserResult.result = "ERROR = ${task.result.toString()}"

                        addFriendResult.value = addUserResult
                    }
                }


            }
            else{
                addUserResult.result = "ERROR = ${task.result.toString()}"

                addFriendResult.value = addUserResult
            }
        }

        return addFriendResult

    }

    override fun deleteFriendRequest(notification: AddFriendNotification) : Observable<Result>{
        // TODO: implement it
        return Observable.create(ObservableOnSubscribe<Result>{
            var result = AddFriendResult("OK")
            it.onNext(result)
        })
    }

    override fun deleteBroadCastotifications() : LiveData<Result>{

        var result = GetUserDataResult("OK")

        mCurrentUser =  FirebaseAuth.getInstance().currentUser

        mDataBase = FirebaseDatabase.getInstance().reference.child("Notifications")
            .child(mCurrentUser!!.uid)

        mDataBase.removeValue().addOnCompleteListener {task->
            if (task.isSuccessful){
                deletenotificationsResult.value = result
            }
            else{
                result.res = "ERROR = ${task.result.toString()}"
            }
        }
        return deletenotificationsResult
    }

    override fun getBroadCaseNotifications() : LiveData<ArrayList<Notification>>{
        var tempList = arrayListOf<Notification>()

        mCurrentUser =  FirebaseAuth.getInstance().currentUser

        mDataBase = FirebaseDatabase.getInstance().reference.child("Notifications")
            .child(mCurrentUser!!.uid)

        mDataBase.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("FirebaseManager:getBroadCaseNotifications","Error = ${p0.message}")
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {snapshot->
                    var message = snapshot.child("Message").value.toString()
                    var notificationType = snapshot.child("NotificationType").value.toString()
                    var source_UName = snapshot.child("Source_UName").value.toString()
                    var uID = snapshot.child("UID").value.toString()
                    var status = snapshot.child("Status").value.toString()
                    var target_UID = snapshot.child("Target_UName").value.toString()
                    var target_UName = snapshot.child("Target_UID").value.toString()

                    when (notificationType){
                        "ADDFRIEND"->{
                            var addFriendNotification = AddFriendNotification(message,"EMPTY")
                            addFriendNotification.sourceUName = source_UName
                            addFriendNotification.targetUName = target_UName
                            addFriendNotification.targetUID = target_UID
                            addFriendNotification.sourceUID = uID
                            addFriendNotification.status = status

                            tempList.add(addFriendNotification)

                        }

                    }

                }
                broadCastNotificationList.value = tempList
            }
        })

        return broadCastNotificationList
    }

    override fun addFriendNotification(notification : AddFriendNotification) : LiveData<Result>
    {
        var userObj = hashMapOf<String,String>()

        mDataBase = FirebaseDatabase.getInstance().reference.child("Notifications").
            child(notification.targetUID).push()

        mDataBase.key?.let {
            userObj.put("NotificationID",it)
        }

        userObj.put("NotificationType", notification.notificationType.toString())
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
    override fun getCurrentUserData() : LiveData<Result>{
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

    override fun addFriendRequest(user : User) : LiveData<Result>{

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

    override fun getNotifications() : LiveData<ArrayList<AddFriendNotification>>{
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
                        var newFriendNotification = AddFriendNotification("New Friend Request From: ${sourceUname}","EMPTY")
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

    override fun deleteFriend(user : User) : Observable<Result>{
        return Observable.create(ObservableOnSubscribe<Result>{emitter->
            var result = GetUserDataResult("OK")

            mCurrentUser =  FirebaseAuth.getInstance().currentUser

            mDataBase = FirebaseDatabase.getInstance().reference.child("Friends")
                .child(mCurrentUser!!.uid).child(user.userID)

            mDataBase.removeValue().addOnCompleteListener {task->
                if (task.isSuccessful){
                    emitter.onNext(result)
                }
                else{
                    Log.d("Error getNotifications()","Error = ${task.result.toString()}")
                    emitter.onError(Throwable(task.result.toString()))
                }
            }
        }
        )

    }

    override fun getNotifications2() : Observable<ArrayList<AddFriendNotification>>{

        return Observable.create(ObservableOnSubscribe<ArrayList<AddFriendNotification>> { emitter->

            mCurrentUser =  FirebaseAuth.getInstance().currentUser

            mDataBase = FirebaseDatabase.getInstance().reference.child("FriendsRequests")
                .child(mCurrentUser!!.uid)

            mDataBase.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("Error getNotifications()","Error = ${p0.message}")
                    emitter.onError(Throwable(p0.message))
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var tempNotificationList = arrayListOf<AddFriendNotification>()

                    p0.children.forEach {snapshot->
                        val sourceUID = snapshot.child("Source_UID").value.toString()
                        val sourceUname = snapshot.child("Source_UName").value.toString()
                        val targetUID = snapshot.child("Target_UID").value.toString()
                        val targetUName = snapshot.child("Target_UName").value.toString()
                        val status = snapshot.child("status").value.toString()

                        if (targetUID == CurrentUser.userID){
                            var newFriendNotification = AddFriendNotification("New Friend Request From: ${sourceUname}","EMPTY")
                            newFriendNotification.sourceUID = sourceUID
                            newFriendNotification.sourceUName = sourceUname
                            newFriendNotification.status = status
                            newFriendNotification.targetUID = targetUID
                            newFriendNotification.targetUName = targetUName

                            tempNotificationList.add(newFriendNotification)
                        }

                        emitter.onNext(tempNotificationList)

                    }
                }
            })
        })

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFriendsList(): Observable<ArrayList<User>> {
        return Observable.create(ObservableOnSubscribe<ArrayList<User>> { emitter->
            mCurrentUser =  FirebaseAuth.getInstance().currentUser

            mDataBase = FirebaseDatabase.getInstance().reference.child("Friends")
                .child(mCurrentUser!!.uid)

            mDataBase.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("Error getNotifications()","Error = ${p0.message}")
                    emitter.onError(Throwable(p0.message))
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var tempUserList = arrayListOf<User>()

                    p0.children.forEach { snapshot ->
                        var displayName = snapshot.child("displayname").value.toString()
                        var imageUrl = snapshot.child("imageUrl").value.toString()
                        var status = snapshot.child("status").value.toString()
                        var thumbimageurl = snapshot.child("thumbimageurl").value.toString()
                        var userID = snapshot.child("userid").value.toString()

                        var user = User(displayName, userID)
                        user.status = status
                        user.thumb_image_url = thumbimageurl
                        user.image_url = imageUrl
                        tempUserList.add(user)
                    }
                    emitter.onNext(tempUserList)
                    emitter.onComplete()
                }})
        })

    }

}
