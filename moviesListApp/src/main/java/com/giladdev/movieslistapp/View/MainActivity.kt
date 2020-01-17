package com.giladdev.movieslistapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.GridLayout
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.giladdev.movieslistapp.R
import com.giladdev.movieslistapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : MainViewModel
    private lateinit var countDownTimer : CountDownTimer
    lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.refresh()

        moviesAdapter = MoviesAdapter(arrayListOf())

        RecycleView.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(context!!, 3)
        }

        setListeners()
        observeViewModel()
        setCacheTimer()
    }

    private fun setCacheTimer(){
        val oneWeekMillis:Long = 1000*60*60*24*7
        val fifteenMinutesMillis:Long = 1000*60*15

        countDownTimer = object : CountDownTimer(oneWeekMillis,fifteenMinutesMillis){
            override fun onFinish() {
                Log.d("Timer","Timer is finished")
            }

            override fun onTick(millisUntilFinished: Long) {
                viewModel.refresh()
            }
        }
    }


    private fun observeViewModel() {
        viewModel.moviesList.observe(this, Observer { movies ->
            movies?.let {
                progressBar.visibility = View.GONE
                RecycleView.visibility = View.VISIBLE
                moviesAdapter.updateMoviers(it)
                Log.d("moviesList", it.toString())

            }
        })

        viewModel.moviesLoading.observe(this, Observer { isloading ->
            isloading?.let {
                progressBar.visibility = if (it) View.GONE else View.VISIBLE
                if (it) {
                    TVError.visibility = View.GONE
                    RecycleView.visibility = View.GONE
                }
            }
        })

        viewModel.moviesError.observe(this, Observer { error ->
            error?.let {
                if (it != "OK") {
                    TVError.visibility = View.VISIBLE
                } else {
                    TVError.visibility = View.GONE
                }
            }
        })
    }

        private fun setListeners(){
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("onQueryTextSubmit", query!!)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("onQueryTextChange", newText!!)
                    moviesAdapter.filter.filter(newText.toLowerCase())
                    return true
                }
            })
        }
    }


