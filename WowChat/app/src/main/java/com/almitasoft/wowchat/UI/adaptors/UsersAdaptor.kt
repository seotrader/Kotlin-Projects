package com.almitasoft.wowchat.UI.adaptors

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.almitasoft.wowchat.R
import com.almitasoft.wowchat.UI.Activities.ChatActivity
import com.almitasoft.wowchat.UI.Activities.ProfileActivity
import com.almitasoft.wowchat.models.Users
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdaptor(databaseQuerty : DatabaseReference,
                   var context: Context,
                   var options: FirebaseRecyclerOptions<Users>,
                   var currentUser : String) : FirebaseRecyclerAdapter<Users, UsersAdaptor.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdaptor.ViewHolder {


        var view = LayoutInflater.from(context).inflate(R.layout.users_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersAdaptor.ViewHolder, position: Int, user: Users) {
        var userId = getRef(position).key// the unique firebase key id of the current user

        holder.bindView(user,context)

        holder.itemView.setOnClickListener {
            // create a popup dialog where user can choose to send a message or to see a profile
            var options = arrayOf("Open Profile", "Send Message")
            var builder = AlertDialog.Builder(context)
                .setTitle("Select Options")
                .setItems(options, DialogInterface.OnClickListener { _, which ->
                    var userName = holder.userNameTxt
                    var userStat = holder.userStatusTxt
                    var profilePic = holder.userProfilePicLink

                    if (which == 0) {
                        // open user profile
                        var profileIntent = Intent(context, ProfileActivity::class.java)
                        profileIntent.putExtra("userId", userId)
                        context.startActivity(profileIntent)
                    } else {
                        var chatIntent = Intent(context, ChatActivity::class.java)
                        chatIntent.putExtra("userId", userId)
                        chatIntent.putExtra("name", userName)
                        chatIntent.putExtra("status", userStat)
                        chatIntent.putExtra("profile", profilePic)
                        context.startActivity(chatIntent)
                    }
                }

                ).show()

        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var userNameTxt: String?= null
        var userStatusTxt: String?=null
        var userProfilePicLink : String?=null


        fun bindView(user : Users, context: Context){
            var userName = itemView.findViewById<TextView>(R.id.userName)
            var userStatus = itemView.findViewById<TextView>(R.id.userStatus)
            var userProfilePic = itemView.findViewById<CircleImageView>(R.id.usersProfileID)

            // set the strings so we can pass in the intent
            userNameTxt = user.display_name
            userStatusTxt = user.user_status
            userProfilePicLink = user.thumb_image

            userName.text = userNameTxt
            userStatus.text = userStatusTxt

            Picasso.get().load(userProfilePicLink.toString())
                .placeholder(R.drawable.profile_img)
                .into(userProfilePic)



        }
    }
}