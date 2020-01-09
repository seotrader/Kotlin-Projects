package com.almitasoft.choremeapp.ui.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.almitasoft.choremeapp.model.CreateAccountResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("NAME_SHADOWING")
class CreateNewAccountViewModel : ViewModel() {
    var mAuth = FirebaseAuth.getInstance()
    lateinit var mDataBase: DatabaseReference
    var accountCreated = MutableLiveData<CreateAccountResult>()

    fun createAccount(
        email: String,
        password: String,
        displayName: String
    ) : LiveData<CreateAccountResult> {
        var result = CreateAccountResult("NO RESULT")

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var currentUser = mAuth.currentUser
                    var userId = currentUser!!.uid
                    var userObj = HashMap<String, String>()

                    mDataBase = FirebaseDatabase.getInstance().reference
                        .child("Users").child(userId)

                    userObj.put("display name", displayName)
                    userObj.put("status", "Hello There...")
                    userObj.put("image", "default")
                    userObj.put("thumb_image", "default")
                    userObj.put("email",email)

                    mDataBase.setValue(userObj).addOnCompleteListener { task ->


                        if (task.isSuccessful) {
                            result.result = "OK"
                            accountCreated.value = result
                        } else {
                            result.result = task.exception.toString()
                            accountCreated.value = result
                        }
                    }
                }
            }
        return accountCreated
    }

}

