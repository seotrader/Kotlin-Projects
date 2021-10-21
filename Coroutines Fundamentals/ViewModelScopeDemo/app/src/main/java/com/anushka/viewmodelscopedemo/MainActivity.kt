package com.anushka.viewmodelscopedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityViewModal: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModal = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        mainActivityViewModal.getUserData()
        mainActivityViewModal.users.observe(this, Observer { myusers->
            myusers.forEach { 
                Log.d("MyTag", "name is ${it.name}")
            }
        })
    }
}
