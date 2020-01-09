package com.almitasoft.choremeapp.ui.login

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.model.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class LoginViewModel : ViewModel() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mDataBase : DatabaseReference
    var mCurrentUser : FirebaseUser?=null

    var loginResult = MutableLiveData<LoginResult>()

    var displayName : String?=null

    fun loginUser(email: String, password: String) : LiveData<LoginResult>{

        mAuth = FirebaseAuth.getInstance()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var userID: String

                        mCurrentUser = FirebaseAuth.getInstance().currentUser

                        mCurrentUser?.let {
                            userID = it.uid
                            mDataBase = FirebaseDatabase.getInstance().reference.child("Users")
                                .child(userID.toString())
                            getUserData()

                        }
                    } else {
                        Log.d("Error Login addOnCompleteListener", task.exception.toString())
                        var result = "${task.exception.toString()}"
                        var lResult = LoginResult(result, displayName.toString())
                        loginResult.value = lResult

                    }
                }
        } else{
            var result = "User and Password Can't be Empty"
            var lResult = LoginResult(result, displayName.toString())
            loginResult.value = lResult

        }

        return loginResult
    }

    fun getUserData(){
        mDataBase.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("On Cancelled","Error = ${p0.message}")
                var result=p0.message
                var lResult = LoginResult(result, "EMPTY")
                loginResult.value = lResult

            }

            override fun onDataChange(snapShot: DataSnapshot) {

                var result = "OK"
                var displayName = snapShot.child("display name").value.toString()
                var image = snapShot.child("image").value.toString()
                var status = snapShot.child("status").value.toString()
                var thumb_image = snapShot.child("thumb_image").value.toString()

                CurrentUser.displanyName = displayName
                CurrentUser.image_url = image
                CurrentUser.thumb_image_url = thumb_image
                CurrentUser.status = status

                var lResult = LoginResult(result, displayName.toString())

                loginResult.value = lResult
            }
        })
    }
}
