package com.giladdev.choreapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.giladdev.choreapp.R
import com.giladdev.choreapp.model.Chore
import com.giladdev.choreapp.model.ChoresDataBaseHandler
import kotlinx.android.synthetic.main.activity_chore_list.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.popup.view.*

class ChoreListActivity : AppCompatActivity() {

    lateinit private var dbHandler: ChoresDataBaseHandler
    lateinit private var dialogBuilder: AlertDialog.Builder
    lateinit private var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_list)

        var choresList = ArrayList<Chore>()
        var choreListItems = ArrayList<Chore>()

        lateinit var choreAdapter : ChoreListAdapter

        choreAdapter = ChoreListAdapter(choreListItems, this)

        choreRecyclerView.apply {
            adapter = choreAdapter
            layoutManager = LinearLayoutManager(context)

        }

        dbHandler = ChoresDataBaseHandler(this)

        choresList = dbHandler.readChores()

        choresList.reverse()

        choreAdapter.notifyDataSetChanged()

        for (c in choresList.iterator()){
            var chore = Chore()

            chore.choreName = "Chore: ${c.choreName}"
            chore.assignedTo = "Assigned To: ${c.assignedTo}"
            chore.assignedBy = "Assigned By: ${c.assignedBy}"
            chore.timerAssigned = c.timerAssigned
            chore.id = c.id
            choreListItems.add(chore)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.topmenu,menu)
        CreatePopupDialog()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_menu_item){
            dialog.show()
        }

        return true
    }

    fun CreatePopupDialog(){
        var view = layoutInflater.inflate(R.layout.popup, null)

        var choreName = view.popEnterChoreText
        var assignedBy = view.popEnterAssignedBy
        var assignedTo = view.popEnterAssignedTo
        var saveButton = view.popUpSaveButton

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder.create()

        saveButton.setOnClickListener {
            var name = choreName.text.toString().trim()
            var aBy = assignedBy.text.toString().trim()
            var aTo = assignedTo.text.toString().trim()

            if (!TextUtils.isEmpty(name)
                && (!TextUtils.isEmpty(aBy))
                && (!TextUtils.isEmpty(aTo))){
                var chore = Chore()
                chore.choreName = name
                chore.assignedBy = aBy
                chore.assignedTo = aTo

                dbHandler.CreateChore(chore)

                dialog.dismiss()

                startActivity(Intent(this,ChoreListActivity::class.java))
                finish()

            }
            else{


        }


        }

    }

}
