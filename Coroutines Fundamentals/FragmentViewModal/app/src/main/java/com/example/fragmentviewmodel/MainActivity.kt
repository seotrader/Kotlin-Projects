package com.example.fragmentviewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import com.example.fragmentviewmodel.ui.main.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        lifecycleScope.launch(Dispatchers.IO){
//            delay(5000)
//            progressBar.visibility = View.VISIBLE
//            delay(10000)
//            progressBar.visibility = View.GONE
            Log..
        }
        
        lifecycleScope.launchWhenCreated { 
            
        }

        lifecycleScope.launchWhenStarted {  }c {

        }
    }
}