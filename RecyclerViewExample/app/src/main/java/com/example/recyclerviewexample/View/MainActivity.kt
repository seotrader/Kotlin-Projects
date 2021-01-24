package com.example.recyclerviewexample.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewexample.R
import com.example.recyclerviewexample.model.City
import com.example.recyclerviewexample.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    var cities = arrayListOf<City>()
    lateinit var cityAdapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val citiesRv = findViewById<RecyclerView>(R.id.recycler_view)

        cityAdapter = CitiesAdapter(cities)

        citiesRv.apply {
            adapter = cityAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCountries().observe(this, Observer<List<City>> {
            Log.d("MainActivity", "Received cities = $it")
            cities.addAll(it)
            cityAdapter.notifyDataSetChanged()

        })
    }
}