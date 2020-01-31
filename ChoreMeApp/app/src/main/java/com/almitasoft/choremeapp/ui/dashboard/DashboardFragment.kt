package com.almitasoft.choremeapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import com.almitasoft.choremeapp.ui.MainActivity
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.model.User
import com.almitasoft.choremeapp.ui.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var mainActivity : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)



        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(this, Observer {
            textView.text = it
        })

        mainActivity = activity as MainActivity

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)

            if (!sharedViewModel.isUserConnected()){
                mainActivity.navController.navigate(R.id.welcome_screen)
            }

            it.fab.show()
        }


        mainActivity.nav_view.visibility = View.VISIBLE

        if (sharedViewModel.isUserConnected()){
            var fbManager = FireBaseManager()

            fbManager.getCurrentUserData().observe(this, Observer {
                if (it.result == "OK") {
                    Log.d("Info","Current User = ${it.toString()}")
                }else{
                    Toast.makeText(mainActivity, "ERROR WITH DBS", Toast.LENGTH_SHORT).show()

                }
            })

            sharedViewModel.getFriendsList().observe(this, object: Observer<ArrayList<User>> {

                override fun onChanged(t: ArrayList<User>?) {
                    CurrentUser.friendsList = t
                }
            })
        }

        return root
    }
}