package com.almitasoft.notesapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_note_editor.*

class NoteEditorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)

        title = "Write Your Note"

       var nodeId : Int = intent.getIntExtra("noteID",-1)

        if (nodeId != -1){
            editText.setText(MainActivity.notes[nodeId])
        }else{
            MainActivity.notes.add("")
            nodeId = MainActivity.notes.size-1
        }

        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                MainActivity.notes[nodeId] = s.toString()
                MainActivity.arrayAdapter!!.notifyDataSetChanged()

                var sharedPreferences = applicationContext.getSharedPreferences("com.almitasoft.notesapp",
                    Context.MODE_PRIVATE)

                val set = hashSetOf<String>()
                set.addAll(MainActivity.notes)

                sharedPreferences.edit().putStringSet("notes",set).apply()

            }
        })
    }
}
