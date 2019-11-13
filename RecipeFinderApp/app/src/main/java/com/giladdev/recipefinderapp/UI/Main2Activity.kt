package com.giladdev.recipefinderapp.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.giladdev.recipefinderapp.R
import kotlinx.android.synthetic.main.activity_main.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainButton.setOnClickListener {
            var intent = Intent(this, ReceiptList::class.java)
            var ingredients = ingridiateEdtText.text.toString().trim()
            var searchTerm = searchTermEdt.text.toString().trim()

            intent.putExtra("ingredients", ingredients)
            intent.putExtra("search", searchTerm)

            startActivity(intent)
        }
    }
}
