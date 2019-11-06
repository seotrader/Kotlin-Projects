package com.giladdev.rickyandmarty.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.giladdev.rickyandmarty.R
import com.giladdev.rickyandmarty.model.CharacterList
import com.giladdev.rickyandmarty.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private var charAdapter =  CharacterListAdaptor(arrayListOf(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        errorTextView.visibility = View.GONE
        listRecyclerView.visibility = View.VISIBLE

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        // apply - Scope Functions
        listRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = charAdapter
        }



        observeViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.connection_mode -> {
                Log.d("Connection Mode", "Connection Mode")
                ShowConnectivityDialog()
            }
            R.id.save ->
                Log.d("Save","Save")
        }

        return super.onOptionsItemSelected(item)
    }

    fun ShowConnectivityDialog()
    {
        val connectivityFreg = ConnectivityDialogFragment()
        connectivityFreg.show(supportFragmentManager,"connectivity")
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.top_menu, menu)

        return true
    }

    fun observeViewModel() {

        charAdapter.lastLine.observe(this, Observer {isLastLine ->
            isLastLine.let{
                if (it == true) {
                    viewModel.refresh()
                }
            }
        })
        viewModel.countryLoadError.observe(this, Observer {isError ->
            if (isError) View.VISIBLE else View.GONE
//            if (is)
//            isError.let{
//                errorTextView.visibility = if (it) View.VISIBLE else View.GONE
//            }
        })
        viewModel.characters.observe(this, androidx.lifecycle.Observer { characters: CharacterList? ->
            characters?.let {
                listRecyclerView.visibility = View.VISIBLE
                charAdapter.updateCharacters(it)}
        })

        viewModel.loading.observe(this, Observer {isLoading ->

            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) errorTextView.visibility = View.GONE

//            if ()
//            isLoading?.let {
//                progressBar.visibility = if (it) View.VISIBLE else View.GONE
//
//                if (it)
//                {
//                    errorTextView.visibility = View.GONE
//                }
//            }
        })
    }
}





