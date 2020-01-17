package com.almitasoft.choremeapp.ui.addusers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.model.AddFriendNotification
import com.almitasoft.choremeapp.model.Notification
import com.almitasoft.choremeapp.model.Result
import com.almitasoft.choremeapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class SearchUsersViewModel : ViewModel(), KoinComponent {

    lateinit var mDataBase : DatabaseReference
    var mCurrentUser : FirebaseUser?=null
    private val fb : FireBaseManager by inject()

    var loadingUsers = MutableLiveData<Boolean>()
    var errorLoading = MutableLiveData<Boolean>()

    fun isUserConnected() = (FirebaseAuth.getInstance().currentUser != null)

    var listOfAllUsers = MutableLiveData<ArrayList<User>>()

    fun addFriendNotification(notification : AddFriendNotification): LiveData<Result>{
        return fb.addFriendNotification(notification)
    }

    fun getListOfUsers() : LiveData<ArrayList<User>>{

        var userList = arrayListOf<User>()

        mDataBase = FirebaseDatabase.getInstance().reference
        mCurrentUser = FirebaseAuth.getInstance().currentUser!!

        userList.clear()

        loadingUsers.value = true

        // in case the current user is connected, go ahead and bring all users
        mCurrentUser?.run{
            mDataBase.child("Users").addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("Firebase Error", p0.message)
                    loadingUsers.value = false
                    errorLoading.value = true
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {snapshot->
                        var displayName = snapshot.child("display name").value.toString()
                        var email = snapshot.child("email").value.toString()
                        var image = snapshot.child("image").value.toString()
                        var status = snapshot.child("status").value.toString()
                        var thumb_image = snapshot.child("status").value.toString()

                        var user = User(displayName,snapshot.key.toString())

                        user.userEmail = email
                        user.status = status
                        user.image_url = image
                        user.thumb_image_url = thumb_image
                        userList.add(user)



                    }
                    Log.d("onDataChange", "Data Read Succesfully")

                    listOfAllUsers.value = userList

                    loadingUsers.value = false
                    errorLoading.value = false
                }
            })
        }

        return listOfAllUsers
    }

    fun getNotificationList() : LiveData<ArrayList<AddFriendNotification>> {
        return fb.getNotifications()
    }
}
