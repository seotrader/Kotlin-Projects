package com.giladdev.countires.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.giladdev.countires.Model.Country
import com.giladdev.countires.R
import com.giladdev.countires.ViewModel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : ListViewModel
    private val countriesAdapter = CountryListAdaptor(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        countrieslist.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing = false
            viewModel.refresh()
        }
        observeViewModel()
    }
    fun observeViewModel()
    {
        viewModel.countires.observe(this, Observer {countries : List<Country>? ->
            countrieslist.visibility = View.VISIBLE
            countries?.let {countriesAdapter.updateCountries(it)}
        }
        )

        viewModel.countryLoadError.observe(this, Observer { iserror:Boolean? ->
            iserror?.let {errorTextView.visibility = if (it) View.VISIBLE else View.GONE}
        })

        viewModel.loading.observe(this, Observer { isLoading:Boolean? ->
            isLoading?.let {
                loadingProgressbar.visibility = if (it) View.VISIBLE else View.GONE
                // if we have a loding
                if (it){
                    errorTextView.visibility = View.GONE
                    countrieslist.visibility = View.GONE
            }

            }})
    }
}


