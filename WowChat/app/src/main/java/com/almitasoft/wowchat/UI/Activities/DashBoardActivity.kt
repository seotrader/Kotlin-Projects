package com.almitasoft.wowchat.UI.Activities

import com.almitasoft.wowchat.UI.adaptors.SectionPageAdapter
import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.almitasoft.wowchat.R
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.almitasoft.wowchat.models.CurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoardActivity : AppCompatActivity() {
    lateinit var mDataBase : DatabaseReference
    lateinit var mCurrentBaseUser: FirebaseUser
    lateinit var  sectionAdapter : SectionPageAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item.itemId == R.id.logoutMenuID){
            // log the user out
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        if (item.itemId == R.id.settingMenuID){
            // take user to settings activity
            startActivity(Intent(this, SettingsActivity::class.java))
        }



        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        mDataBase = FirebaseDatabase.getInstance().reference
        mCurrentBaseUser = FirebaseAuth.getInstance().currentUser!!

        mCurrentBaseUser.let{
            mDataBase.child("Users").child(it.uid).addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(applicationContext,"Data Base Error ${p0.message}",Toast.LENGTH_SHORT).show()
                    Log.d("ERROR DBS", "error = ${p0.message}")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var displayName = p0.child("display name").value.toString().trim()
                    var thumb_image = p0.child("thumb_image").value.toString().trim()

                    title = "Welcome $displayName"
                    CurrentUser.user.displayName = displayName
                    CurrentUser.user.userID = mCurrentBaseUser.uid
                    CurrentUser.user.thumb_image = thumb_image

                }
            })
        }


        sectionAdapter = SectionPageAdapter(supportFragmentManager)

        dashBoardViewPager.adapter = sectionAdapter

        TabParent.setupWithViewPager(dashBoardViewPager)

        TabParent.setTabTextColors(Color.WHITE,Color.GREEN)


        var data = intent.extras

        data?.let{
            var username = it.get("name")

            Toast.makeText(this, username.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }
}

