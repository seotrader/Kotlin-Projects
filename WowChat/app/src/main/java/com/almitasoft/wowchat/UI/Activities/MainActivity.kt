package com.almitasoft.wowchat.UI.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.almitasoft.wowchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mAuth : FirebaseAuth?=null
    var mAuthListener : FirebaseAuth.AuthStateListener?=null
    val REQUEST_CODE_EXTERNAL_STORAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        mAuth = FirebaseAuth.getInstance()
        var user : FirebaseUser?  = mAuth!!.currentUser

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CheckPermissions()

        createActButon.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java ))
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java ))
        }

        if (user != null){
            // let's go to dashboard
            startActivity(Intent(this, DashBoardActivity::class.java))
            finish()
        }else{
            //Toast.makeText(this, "Not Signed In",Toast.LENGTH_LONG).show()
        }
        //}
    }

    fun CheckPermissions(){



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Toast.makeText(applicationContext,"No Permission external storage",
                Toast.LENGTH_LONG).show()
        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        } else {
            // Permission has already been granted
//            Toast.makeText(applicationContext, "Permission To Read Storage was already Granted",
//                Toast.LENGTH_LONG).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(applicationContext, "Permission To Read Storage Granted",
                        Toast.LENGTH_LONG).show()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    override fun onStart() {
        super.onStart()
        mAuthListener = FirebaseAuth.AuthStateListener {_ ->
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
