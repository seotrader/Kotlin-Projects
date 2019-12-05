package com.almitasoft.wowchat.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.almitasoft.wowchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_status.*

class StatusActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mDataBase : DatabaseReference
    var mCurrentUser : FirebaseUser?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_status)
        supportActionBar?.let{
            it.title = "Status"
        }
            if (intent.extras != null){
                var oldStatus = intent.extras!!.get("status")
                statusUpdateEdt.setText(oldStatus.toString())
            }
            if (intent.extras?.equals(null)!!){
                statusUpdateEdt.setText("Enter Your New Status")
            }

            statusUpdateBtn.setOnClickListener {
                mCurrentUser = FirebaseAuth.getInstance().currentUser
                var userID = mCurrentUser!!.uid

                mDataBase = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(userID)
                var status = statusUpdateEdt.text.toString().trim()

                mDataBase.child("status")
                    .setValue(status).addOnCompleteListener {task->
                        if (task.isSuccessful){
                            Toast.makeText(this, "Status Updated Successfully!",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,
                                SettingsActivity::class.java))
                        }else {
                            Toast.makeText(this, "Status Not Updated!",Toast.LENGTH_LONG).show()
                        }
                    }

            }
        }
}
