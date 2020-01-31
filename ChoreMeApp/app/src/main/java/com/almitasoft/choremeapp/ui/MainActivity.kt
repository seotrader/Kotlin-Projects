package com.almitasoft.choremeapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import com.almitasoft.choremeapp.Notifications.NotificationSender
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.di.appModule
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.model.NotificationType
import com.almitasoft.choremeapp.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_with_menu.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.core.parameter.parametersOf


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SharedViewModel
    lateinit var navController : NavController
    lateinit var appBarConfiguration : AppBarConfiguration
    private val notificationService :  NotificationSender by inject { parametersOf(
        this) }

    init{
        stopKoin()
        startKoin{
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_with_menu)

        viewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navTopView: NavigationView = findViewById(R.id.top_view)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)


        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_friends
            ), drawerLayout
        )

        navTopView.setNavigationItemSelectedListener {menuItem->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }

        fab.show()
        fab.setOnClickListener { view ->
            navController.navigate(R.id.addTask)

        }

        setupWithNavController(navTopView,navController)
        setupWithNavController(nav_view,navController)
        setupActionBarWithNavController(this,navController, appBarConfiguration)
        //navView.setupWithNavController(navController)

        setNotificationChannel()


        if (viewModel.isUserConnected()){

            observeNotifications()
            setObservers()
        }

        printWindowSize()
    }

    private fun setObservers(){

        viewModel.getFriendsList().observe(this, object: Observer<ArrayList<User>> {

            override fun onChanged(t: ArrayList<User>?) {
                CurrentUser.friendsList = t
            }
        })

    }

    fun observeNotifications(){
        viewModel.getBroadCastNotifications().observe(this, Observer {
            it.forEach {notification->
                when (notification.notificationType) {
                    NotificationType.ADDFRIEND -> {
                        notificationService.Notify(notification.notificationMessage,0)
                    }
                    NotificationType.NEWFRIENDNOTIFICATION -> {

                    }
                    NotificationType.ADDTASK -> {
                    }
                    NotificationType.GENERALNOTIFICATION ->{
                        notificationService.Notify(notification.notificationMessage,0)
                    }
                }
            }
            viewModel.deleteBroadCastNotification().observe(this, Observer {result->
                if (result.result=="OK"){
                    Log.d("MainActivity:observeNotification","OK")
                }else{
                    Log.d("MainActivity:observeNotification","ERROR = ${result.result}")
                }

            })
        })
    }
    fun printWindowSize(){
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        val dpHeight = outMetrics.heightPixels / density
        val dpWidth = outMetrics.widthPixels / density

        Toast.makeText(this, "Height = $dpHeight, width=$dpWidth", Toast.LENGTH_LONG).show()
    }

    fun getNotification(){

    }
    fun setNotificationChannel(){

        notificationService.removeNotificationChannel()
        notificationService.createNotificationChannel()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        //return navigateUp(navController,drawer_layout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)

        var searchItem = menu!!.findItem(R.id.search_btn)
        var searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Terms/Title/Genre"


        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updatedFilteredText.value = newText
                return true
            }
        })

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logoutID){
            // log the user out
            FirebaseAuth.getInstance().signOut()
            navController.navigate(R.id.welcome_screen)
        }else{
            if (item.itemId == R.id.menuUserSettings){
                navController.navigate(R.id.user_settings)
            }
        }


        if (item.itemId == R.id.search_btn){
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("onActivityResult", "Request code = $requestCode")

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            viewModel.cropeActivityResult.value = CropImage.getActivityResult(data)

        }
    }

    override fun onResume(){
        viewModel.mainActivity.value = this
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}

