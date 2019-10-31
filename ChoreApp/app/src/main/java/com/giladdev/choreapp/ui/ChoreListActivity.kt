package com.giladdev.choreapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.giladdev.choreapp.R
import com.giladdev.choreapp.viewmodel.ChoresViewModel
import kotlinx.android.synthetic.main.activity_chore_list.*
import androidx.lifecycle.ViewModelProviders
import com.giladdev.choreapp.model.ChoresEntity
import kotlinx.android.synthetic.main.popup.view.*

class ChoreListActivity : AppCompatActivity() {

    lateinit private var dialogBuilder: AlertDialog.Builder
    lateinit private var dialog: AlertDialog
    lateinit var choresViewModel : ChoresViewModel
    lateinit var choreAdapter : ChoreListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_list)

        var choreListItems = ArrayList<ChoresEntity>()

        choreAdapter = ChoreListAdapter(choreListItems, this)

        choreRecyclerView.apply {
            adapter = choreAdapter
            layoutManager = LinearLayoutManager(context)

        }

        choresViewModel = ViewModelProviders.of(this).get(ChoresViewModel::class.java)
        choresViewModel.mContext = this
        choresViewModel.refresh()

        observeViewModel()


    }

    fun observeViewModel()
    {
        choresViewModel.choresList.observe(this, Observer {list:List<ChoresEntity>->
            list.let{
                choreAdapter.UpdateChores(it)
            }
        } )

        choresViewModel.deleteChore.observe(this, Observer {toDelete:Boolean->
            toDelete.let{
                if (it==true){
                    Toast.makeText(this, "Chore Deleted!",Toast.LENGTH_LONG).show()
                    choresViewModel.deleteChore.value = false
                }
            }})
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
            val currentTime = System.currentTimeMillis()

            if (!TextUtils.isEmpty(name)
                && (!TextUtils.isEmpty(aBy))
                && (!TextUtils.isEmpty(aTo))){
                var chore = ChoresEntity(choreName = name, assignedTo = aTo, assignedBy = aBy, timerAssigned = currentTime)

                choresViewModel.AddChore(chore)

                dialog.dismiss()

                startActivity(Intent(this,ChoreListActivity::class.java))

                finish()
            }
            else{
                Toast.makeText(this, "Fill All Fields ... ",Toast.LENGTH_LONG).show()


            }


        }

    }

}
