package com.almitasoft.wowchat.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.almitasoft.wowchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    lateinit var mUsersDataBaseRef : DatabaseReference
    var mCurrentUser : FirebaseUser?= null
    lateinit var userId : String

    var displayName: String?=null
    var thumbImageUrl: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null){
            userId = intent.extras!!.get("userId").toString()
            mCurrentUser = FirebaseAuth.getInstance().currentUser
            mUsersDataBaseRef = FirebaseDatabase.getInstance().reference.child("Users").
                child(userId)

            setUpProfile()

            sendMessageBTN.setOnClickListener {
                var chatIntent = Intent(this, ChatActivity::class.java)
                chatIntent.putExtra("userId", userId)
                chatIntent.putExtra("name", displayName)
                chatIntent.putExtra("profile", thumbImageUrl)
                this.startActivity(chatIntent)
            }
        }
    }

    private fun setUpProfile() {
        mUsersDataBaseRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                displayName  = dataSnapShot.child("display name").value.toString()
                var status = dataSnapShot.child("status").value.toString()
                var image = dataSnapShot.child("image").value.toString()
                thumbImageUrl = dataSnapShot.child("thumb_image").value.toString()

                profileNameTV.text = displayName
                profileStatusTV.text = status

                Picasso.get().load(image)
                    .placeholder(R.drawable.profile_img)
                    .into(profileImage)
            }
        }
        )
    }
}
