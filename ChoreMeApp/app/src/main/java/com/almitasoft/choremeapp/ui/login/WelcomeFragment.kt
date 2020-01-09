package com.almitasoft.choremeapp.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import com.almitasoft.choremeapp.ui.MainActivity
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.ui.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.welcome_fragment.*

class WelcomeFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    private lateinit var viewModel: WelcomeViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WelcomeViewModel::class.java)
        // TODO: Use the ViewModel

        activity?.let{
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)

            this.mainActivity = activity as MainActivity

            it.nav_view.visibility = View.GONE

            it.fab.visibility = View.GONE
        }

        if (sharedViewModel.isUserConnected()){
            var fbManager = FireBaseManager()

            fbManager.getCurrentUserData().observe(this, Observer {
                if (it.result == "OK") {
                    mainActivity.navController.navigate(R.id.navigation_dashboard)
                }else{
                    Toast.makeText(mainActivity, "ERROR WITH DBS",Toast.LENGTH_SHORT).show()
                }
            })


        }

        loginButton.setOnClickListener {
            var mainActivity = activity as MainActivity
            mainActivity.navController.navigate(R.id.login_screen)
        }

        createActButon.setOnClickListener {
            var mainActivity = activity as MainActivity
            mainActivity.navController.navigate(R.id.create_new_account)
        }
    }

}
