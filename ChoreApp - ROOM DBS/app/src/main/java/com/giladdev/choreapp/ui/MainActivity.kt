package com.giladdev.choreapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.giladdev.choreapp.R
import com.giladdev.choreapp.model.Chore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var enterChore = enterChoreeditText
        var assignedBy = assignedByTextEdit
        var assignedTo = assignedtoEditText
        var saveChore = saveChorebutton


        startActivity(Intent(this, ChoreListActivity::class.java))

    }

    fun checkDB(){
    }

    fun saveToDB(chore :Chore) {
    }
}
