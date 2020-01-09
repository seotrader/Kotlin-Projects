package com.almitasoft.choremeapp.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.almitasoft.choremeapp.ui.MainActivity

import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.CurrentUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment(), View.OnClickListener  {

    private lateinit var mainActivity: MainActivity


    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onClick(v: View?) {
        var id = -1

        v?.let{
            id = it.id
        }

        when (id){
            R.id.loginButtonLA -> {

                var email = emailEditBoxLA.text.toString().trim()
                var password = loginpasswordEditBoxLA.text.toString().trim()

                viewModel.loginUser(email, password).observe(this, Observer {
                    if (it.result != "OK"){
                        Toast.makeText(activity, "Error Login = ${it.result}",
                            Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(activity, "Logged in = ${it.displayName}",
                            Toast.LENGTH_SHORT).show()

                        CurrentUser.userEmail = email
                        // move to Dashboard
                        mainActivity.navController.navigate(R.id.navigation_dashboard)

                    }
                })

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        mainActivity = activity as MainActivity
    }

    override fun onStart() {
        loginButtonLA.setOnClickListener(this)
        super.onStart()
    }
}
