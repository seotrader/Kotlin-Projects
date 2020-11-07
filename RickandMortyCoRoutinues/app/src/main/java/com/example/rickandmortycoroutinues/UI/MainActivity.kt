package com.example.rickandmortycoroutinues.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortycoroutinues.R
import com.example.rickandmortycoroutinues.ViewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    private var charAdapter =  CharactersAdapter(arrayListOf(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel.generateDummyList()

        mainViewModel.characters.observe(this, Observer {
            Log.d("MainActivity",it.characterList.toString())
            charAdapter.updateRV(ArrayList(it.characterList))
        })

        val rv = this.findViewById<RecyclerView>(R.id.mainRecyclerView)

        rv.apply {
            adapter = charAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}