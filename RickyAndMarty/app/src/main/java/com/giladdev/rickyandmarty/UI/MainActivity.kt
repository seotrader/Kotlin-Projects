package com.giladdev.rickyandmarty.UI

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.giladdev.rickyandmarty.R
import com.giladdev.rickyandmarty.model.CharacterList
import com.giladdev.rickyandmarty.viewmodel.ConnectionMode
import com.giladdev.rickyandmarty.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private var charAdapter =  CharacterListAdaptor(arrayListOf(),this)
    lateinit var connectivityMgr : ConnectivityManager// ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var isConnectedToInternet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        errorTextView.visibility = View.GONE
        listRecyclerView.visibility = View.VISIBLE

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.connMode = ConnectionMode.OFFLINE
        viewModel.refresh()
        charAdapter.connMode = viewModel.connMode

        // apply - Scope Functions
        listRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = charAdapter

        }
        observeViewModel()
    }

    override fun onResume() {

        connectivityMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        observerConnectivity()
        super.onResume()
    }
    fun observerConnectivity(){
        var builder : NetworkRequest.Builder = NetworkRequest.Builder()

        connectivityMgr.registerNetworkCallback(builder.build(),object : ConnectivityManager.NetworkCallback(){
            override fun onLost(network: Network?) {
                isConnectedToInternet = false
                //record wi-fi disconnect event
                Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION !!!!", Toast.LENGTH_LONG).show()
            }
            override fun onUnavailable() {
                isConnectedToInternet = false
                Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION !!!!", Toast.LENGTH_LONG).show()

            }
            override fun onLosing(network: Network?, maxMsToLive: Int) {
            }
            override fun onAvailable(network: Network?) {

                isConnectedToInternet = true
                Toast.makeText(getApplicationContext(), "YOU HAVE INTERNET CONNECTION :)", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.connection_mode -> {
                Log.d("Connection Mode", "Connection Mode")
                if (viewModel.loading.value == false)
                    showConnectivityDialog()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun showConnectivityDialog()
    {
        val connectivityFreg = ConnectivityDialogFragment(this,viewModel.connMode)

        connectivityFreg.conneMode.observe(this, Observer {connection ->
            if (viewModel.connMode != connection){
                viewModel.connMode = connection
                charAdapter.connMode = connection
                charAdapter.clear()
                viewModel.refresh()
            }
        })
        connectivityFreg.show(supportFragmentManager,"connectivity")


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    fun observeViewModel() {

        charAdapter.lastLine.observe(this, Observer {isLastLine ->
            isLastLine.let{
                if ((it == true) && (viewModel.connMode == ConnectionMode.ONLINE)){
                    viewModel.refreshMoreLines()
                }
            }
        })
        viewModel.countryLoadError.observe(this, Observer {isError ->
            if (isError) View.VISIBLE else View.GONE
        })
        viewModel.characters.observe(this, androidx.lifecycle.Observer { characters: CharacterList? ->
            characters?.let {
                listRecyclerView.visibility = View.VISIBLE
                charAdapter.updateCharacters(it)
            }})

        viewModel.loading.observe(this, Observer {isLoading ->

            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) errorTextView.visibility = View.GONE
        })
    }
}