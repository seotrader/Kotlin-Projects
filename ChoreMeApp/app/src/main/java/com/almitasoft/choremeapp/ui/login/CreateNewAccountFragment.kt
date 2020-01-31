package com.almitasoft.choremeapp.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer

import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.ui.MainActivity
import kotlinx.android.synthetic.main.create_new_account_fragment.*

class CreateNewAccountFragment : Fragment() {

    companion object {
        fun newInstance() = CreateNewAccountFragment()
    }

    lateinit var mainActivity : MainActivity

    private lateinit var viewModel: CreateNewAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_new_account_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateNewAccountViewModel::class.java)

        mainActivity = activity as MainActivity

        accountCreateBtn.setOnClickListener {
            var email = accountEmailNameET.text.toString().trim()
            var password = accountPasswordET.text.toString().trim()
            var displayName = accountDisplayNameET.text.toString().trim()

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)
                || !TextUtils.isEmpty(displayName)
            ) {
                viewModel.createAccount(email, password, displayName).observe(this, Observer {
                    Toast.makeText(activity, "Result = ${it.result}", Toast.LENGTH_LONG).show()
                    if (it.result == "OK"){
                        mainActivity.navController.navigate(R.id.navigation_dashboard)
                    }
                })
            }
        }
    }
}