package com.almitasoft.wowchat.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.almitasoft.wowchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mAuth : FirebaseAuth?=null
    var mAuthListener : FirebaseAuth.AuthStateListener?=null


    override fun onCreate(savedInstanceState: Bundle?) {

        mAuth = FirebaseAuth.getInstance()
        var user : FirebaseUser?  = mAuth!!.currentUser

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createActButon.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java ))
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java ))
        }

        if (user != null){

            var displayName = user.displayName
            // let's go to dashboard
            startActivity(Intent(this, DashBoardActivity::class.java))
            finish()
        }else{
            Toast.makeText(this, "Not Signed In",Toast.LENGTH_LONG).show()
        }
        //}
    }

    override fun onStart() {
        super.onStart()
        mAuthListener = FirebaseAuth.AuthStateListener {firebaseAuth->
            Log.d("WHY","WHY")
        }
        mAuth!!.addAuthStateListener { mAuthListener }
    }

    override fun onStop() {
        super.onStop()

        if (mAuthListener != null){
            mAuth!!.removeAuthStateListener (mAuthListener!!)
        }
    }
}
