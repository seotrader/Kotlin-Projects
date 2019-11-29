package UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.giladdev.wowchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var mDataBase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()

        accountCreateBtn.setOnClickListener {
            var email = accountEmailNameET.text.toString().trim()
            var password = accountPasswordET.text.toString().trim()
            var displayName = accountDisplayNameET.text.toString().trim()

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)
                    || !TextUtils.isEmpty(displayName)){
                createAccount(email,password,displayName)
            } else{
                Toast.makeText(this, "Please fill out the fields", Toast.LENGTH_LONG)
                        .show()
            }

        }
    }

    fun createAccount(email : String, password: String,displayName : String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {task->
                    if (task.isSuccessful){
                        var currentUser = mAuth.currentUser
                        var userId = currentUser!!.uid
                        var userObj = HashMap<String, String>()

                        mDataBase = FirebaseDatabase.getInstance().reference
                                .child("Users").child(userId)

                        userObj.put("display name", displayName)
                        userObj.put("status","Hello there...")
                        userObj.put("image","default")
                        userObj.put("thumb_image", "default")

                        mDataBase.setValue(userObj).addOnCompleteListener {task->
                            if (task.isSuccessful){
                                var dashboardIntent = Intent(this, DashBoardActivity::class.java)
                                dashboardIntent.putExtra("name", displayName)
                                startActivity(dashboardIntent)
                                finish()
                            }else{
                                Toast.makeText(this, "User not created", Toast.LENGTH_LONG)
                                        .show()
                            }
                        }
                                                                      /*
                            Users
                              - 55w35454d
                                - display name: Paulo
                                - "Hello there"
                                - " image url.."
                         */

                    }else{

                        Log.d("task",task.exception.toString())
                        Toast.makeText(this, "ERROR WITH CALLING CREATE ACCOUNT",
                                Toast.LENGTH_LONG).show()


                    }
                }
    }
}
