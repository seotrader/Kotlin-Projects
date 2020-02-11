package com.almitasoft.notesapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    companion object{
        var notes = arrayListOf<String>()
        var arrayAdapter: ArrayAdapter<String>?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Simple Notes App"
        notes.clear()
        sharedPreferences = applicationContext.getSharedPreferences("com.almitasoft.notesapp",
            Context.MODE_PRIVATE)

        var set : HashSet<String>?=null
        set = sharedPreferences.getStringSet("notes",null) as? HashSet<String>

        set?.let{
            notes.addAll(it)
        } ?: run {
           notes.add("Example Note")
        }

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        listView.adapter = arrayAdapter

        sharedPreferences = applicationContext.getSharedPreferences("com.almitasoft.notesapp",
            Context.MODE_PRIVATE)

        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var intent = Intent(applicationContext, NoteEditorActivity::class.java)
                intent.putExtra("noteID", position)
                startActivity(intent)
            }
        })

        listView.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ): Boolean {
                val builder = AlertDialog.Builder(this@MainActivity)

                builder.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to delete this note?")
                    .setPositiveButton("Yes", object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            notes.removeAt(position)
                            arrayAdapter!!.notifyDataSetChanged()
                            val set = hashSetOf<String>()
                            set.addAll(notes)

                            sharedPreferences.edit().putStringSet("notes",set).apply()
                        }
                    })
                    .setNegativeButton("No",null)
                    .show()

                return true
            }
        })

        setListeners()

    }

    private fun setListeners() {
        fab.setOnClickListener {
            val intent = Intent(applicationContext, NoteEditorActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item.itemId == R.id.add_note){
            var intent = Intent(applicationContext, NoteEditorActivity::class.java)
            startActivity(intent)
            return true
        }else{
            return false
        }


    }
}
