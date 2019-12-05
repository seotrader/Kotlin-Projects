package com.almitasoft.wowchat.UI.adaptors

import android.content.Context
import android.renderscript.Sampler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.almitasoft.wowchat.R
import com.almitasoft.wowchat.models.FriendlyMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ChatsAdaptor(private val list : ArrayList<FriendlyMessage>,
                   private val context: Context) : RecyclerView.Adapter<ChatsAdaptor.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var displayName = itemView.findViewById<TextView>(R.id.userDisplayNameTV)
        var chatText = itemView.findViewById<TextView>(R.id.lastLinesTV)
        var userThumbImage = itemView.findViewById<ImageView>(R.id.userImageView)

        fun bindView(item : FriendlyMessage){
            displayName.text = "Chat With "+item.from
            chatText.text = item.text
            var thumbImageUrl : String?=null

            var mUserDatabase : DatabaseReference?=null
            var currentUser : FirebaseUser?= null

            currentUser = FirebaseAuth.getInstance().currentUser

            currentUser?.let{
                mUserDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(item.id!!)

                mUserDatabase!!.addValueEventListener(object: ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            Log.d("DBS ERROR","Error = ${p0.message}")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            thumbImageUrl = p0.child("thumb_image").value.toString()

                            Picasso.get().load(thumbImageUrl.toString())
                                .placeholder(R.drawable.profile_img)
                                .into(userThumbImage)

                        }

                    })
            }

            // add image later
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.openchat_row,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
   }
}