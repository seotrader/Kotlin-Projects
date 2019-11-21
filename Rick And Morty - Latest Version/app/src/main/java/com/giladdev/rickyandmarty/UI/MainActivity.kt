package com.giladdev.rickyandmarty.UI

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.net.Uri
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
import com.giladdev.rickyandmarty.UI.Fragments.LandscapeFragment
import com.giladdev.rickyandmarty.model.CharacterList
import com.giladdev.rickyandmarty.viewmodel.ConnectionMode
import com.giladdev.rickyandmarty.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.content.res.Configuration
import androidx.fragment.app.FragmentTransaction
import com.giladdev.rickyandmarty.R
import com.giladdev.rickyandmarty.UI.Fragments.ConnectivityDialogFragment
import com.giladdev.rickyandmarty.di.appModule
import kotlinx.android.synthetic.main.fragment_landscape_list_view.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class MainActivity : AppCompatActivity(), LandscapeFragment.OnFragmentInteractionListener{

    lateinit var viewModel: ListViewModel
    private var charAdapter =  CharacterListAdaptor(arrayListOf(),this)
    lateinit var connectivityMgr : ConnectivityManager// ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var isConnectedToInternet = false

    var landscapeFragment : LandscapeFragment?=null

    init{
        stopKoin()
        startKoin{
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        errorTextView.visibility = View.GONE
        listRecyclerView.visibility = View.VISIBLE

        CreateFragments()

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

    fun CreateFragments() {

        val orientation = resources.configuration.orientation

        var transaction = supportFragmentManager.beginTransaction()

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            landscapeFragment = LandscapeFragment.newInstance("none", "none")

            transaction.replace(R.id.topFrame, landscapeFragment!!,"landscapefragment")
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.disallowAddToBackStack()
            transaction.commit()
            mainapplayout.visibility = View.GONE
        } else {
            mainapplayout.visibility = View.VISIBLE

            supportFragmentManager.findFragmentById(R.id.topFrame)?.let {
                supportFragmentManager.beginTransaction()
                    .remove(it).commit()
            }

            var frg = supportFragmentManager.findFragmentByTag("landscapefragment")
            frg?.let{
                transaction.remove(frg)
                transaction.disallowAddToBackStack()
                transaction.commitAllowingStateLoss()
                errorText?.let {
                    it.visibility = View.GONE
                }

            }

        }
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
                //Toast.makeText(getApplicationContext(), "YOU HAVE INTERNET CONNECTION :)", Toast.LENGTH_LONG).show()
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
        val connectivityFreg = ConnectivityDialogFragment(
            this,
            viewModel.connMode
        )

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
                progressBar.visibility = View.GONE
                errorTextView.visibility = View.GONE
                charAdapter.updateCharacters(it)
            }})

        viewModel.loading.observe(this, Observer {isLoading ->

            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading)
            {
                errorTextView.visibility = View.GONE
                listRecyclerView.visibility = View.GONE
            }
            else{
                errorTextView.visibility = View.VISIBLE
                listRecyclerView.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroy() {
        viewModel.unSubscribe()
        super.onDestroy()
    }

    override fun onFragmentInteraction(uri: Uri) {
        Log.d("Fragment","On fragment Interaction ")
    }
}