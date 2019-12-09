package com.almitasoft.wowchat.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.almitasoft.wowchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mDataBase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        forgotPasswordText.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }

        loginButtonLA.setOnClickListener {
            var email = emailEditBoxLA.text.toString().trim()
            var password = loginpasswordEditBoxLA.text.toString().trim()

            mAuth = FirebaseAuth.getInstance()

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                loginUser(email, password)
            } else{
                Toast.makeText(this, "Sorry, login failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {task->
                    if (task.isSuccessful){
                        var username = email.split("@")[0]
                        var dashboardIntent = Intent(this, DashBoardActivity::class.java)
                        dashboardIntent.putExtra("name", username)
                        startActivity(dashboardIntent)
                        finish()
                    }else{
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                }

        Log.d("M-Auth", mAuth.toString())
    }
}
