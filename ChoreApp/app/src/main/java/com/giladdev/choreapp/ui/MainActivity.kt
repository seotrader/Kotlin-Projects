package com.giladdev.choreapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giladdev.choreapp.R
import com.giladdev.choreapp.model.ChoresDataBaseHandler
import com.giladdev.choreapp.model.Chore
import kotlinx.android.synthetic.main.activity_chore_list.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var dbHandler: ChoresDataBaseHandler



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHandler = ChoresDataBaseHandler(this)

        var enterChore = enterChoreeditText
        var assignedBy = assignedByTextEdit
        var assignedTo = assignedtoEditText
        var saveChore = saveChorebutton


        checkDB()

        saveChore.setOnClickListener {

            if (!TextUtils.isEmpty(enterChore.text.toString()) &&
                !TextUtils.isEmpty(assignedBy.text.toString()) &&
                !TextUtils.isEmpty(assignedTo.text.toString())){

                var chore = Chore()

                chore.choreName = enterChore.text.toString()
                chore.assignedBy = assignedBy.text.toString()
                chore.assignedTo = assignedTo.text.toString()
                progressBar.visibility = View.VISIBLE

                saveToDB(chore)

                progressBar.visibility = View.INVISIBLE

                startActivity(Intent(this, ChoreListActivity::class.java))

            }
            else
            {
                Toast.makeText(this, "Please enter a chore", Toast.LENGTH_LONG).show()
            }
        }
}

    fun checkDB(){
        if (dbHandler.GetChoresCount()>0)
        {
            startActivity(Intent(this, ChoreListActivity::class.java))
        }
    }

    fun saveToDB(chore :Chore) {
        dbHandler.CreateChore(chore)
    }

}
