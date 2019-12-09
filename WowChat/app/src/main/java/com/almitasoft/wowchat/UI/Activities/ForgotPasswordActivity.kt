package com.almitasoft.wowchat.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.almitasoft.wowchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mDataBase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        title = "Password Recovery"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()

        recoverEmailBtn.setOnClickListener {
            if (!TextUtils.isEmpty(recoverEmailEdt.text.toString())){

                mAuth.sendPasswordResetEmail(recoverEmailEdt.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Auth FireBase", "Email sent.")
                            Toast.makeText(applicationContext,"Password reset sent to ${recoverEmailEdt.text.toString()}",
                                Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(applicationContext,"Error recover password = ${task.exception.toString()}"
                                ,Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(applicationContext,"Please Enter Email!"
                    ,Toast.LENGTH_LONG).show()

            }
        }


    }


}
