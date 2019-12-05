package com.almitasoft.wowchat.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.almitasoft.wowchat.R
import com.almitasoft.wowchat.models.FriendlyMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.custome_bar_image.view.*

class ChatActivity : AppCompatActivity() {

    lateinit var userID : String
    lateinit var mFireBaseDatabaseRef: DatabaseReference
    lateinit var mFireBaseDataMessagesRef: DatabaseReference
    var mFireBaseUser : FirebaseUser?= null

    lateinit var mLinearLayoutManager : LinearLayoutManager
    lateinit var mFireBaseAdapter : FirebaseRecyclerAdapter<FriendlyMessage,MessageViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mFireBaseUser = FirebaseAuth.getInstance().currentUser

        userID = intent.extras!!.getString("userId").toString()
        var profileImgLink = intent.extras!!.get("profile").toString()
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // enable to attach custome display
        supportActionBar!!.setDisplayShowCustomEnabled(true)

        var inflator = this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater

        var actionBarView = inflator.inflate(R.layout.custome_bar_image, null)

        actionBarView.custom_bar_name.text = intent.extras!!.getString("name")

        supportActionBar!!.customView = actionBarView

        Picasso.get().load(profileImgLink)
            .placeholder(R.drawable.profile_img)
            .into(actionBarView.customBarCircleImage)

        mFireBaseDatabaseRef = FirebaseDatabase.getInstance().reference
        mFireBaseDataMessagesRef = FirebaseDatabase.getInstance().reference
            .child("messages")
            .child(userID.toString())

        var options : FirebaseRecyclerOptions<FriendlyMessage> =
            FirebaseRecyclerOptions.Builder<FriendlyMessage>()
                .setQuery(mFireBaseDataMessagesRef.orderByChild("dest_id").equalTo(String().returnJoinString(userID,mFireBaseUser!!.uid))
                    , SnapshotParser {
                        FriendlyMessage(it.child("id").value.toString(),
                            it.child("dest_id").value.toString(),
                            it.child("text").value.toString(),
                            it.child("from").value.toString(),
                            it.child("to").value.toString())
                    }).build()

        mFireBaseAdapter = object : FirebaseRecyclerAdapter<FriendlyMessage,MessageViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
                var view = LayoutInflater.from(applicationContext).inflate(R.layout.item_message,parent,false)
                return MessageViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: MessageViewHolder,
                position: Int,
                friendlyMessage: FriendlyMessage
            ) {
                if (friendlyMessage != null){
                    holder!!.bindView(friendlyMessage)

                    var currentUserID = mFireBaseUser!!.uid

                    var isMe: Boolean = friendlyMessage.id.equals(currentUserID)

                    if (isMe){
                        // Me to the right side
                        holder.profileImageViewRight!!.visibility = View.VISIBLE
                        holder.profileImageView!!.visibility = View.GONE
                        holder.messageTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.RIGHT)
                        holder.messengerTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.RIGHT)

                        // get image URL , for me
                        mFireBaseDatabaseRef!!.child("Users")
                            .child(currentUserID)
                            .addValueEventListener(object: ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(dataSnapShot: DataSnapshot) {
                                    var imageUrl = dataSnapShot.child("thumb_image").value.toString()
                                    var displayName = dataSnapShot.child("display name").value.toString()

                                    holder.messengerTextView!!.text = "I wrote..."

                                    Picasso.get().load(imageUrl)
                                        .placeholder(R.drawable.profile_img)
                                        .into(holder.profileImageViewRight)

                                }
                            })
                    }else{
                        // to the other person , show image view to the left side
                        // Me to the right side
                        holder.profileImageViewRight!!.visibility = View.GONE
                        holder.profileImageView!!.visibility = View.VISIBLE
                        holder.messageTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.LEFT)
                        holder.messengerTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.LEFT)

                        // get image URL , for me
                        mFireBaseDatabaseRef!!.child("Users")
                            .child(userID)
                            .addValueEventListener(object: ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(dataSnapShot: DataSnapshot) {
                                    var imageUrl = dataSnapShot.child("thumb_image").value.toString()
                                    var displayName = dataSnapShot.child("display name").value.toString()

                                    holder.messengerTextView!!.text =
                                        "${friendlyMessage.from} wrote..."

                                    Picasso.get().load(imageUrl.toString())
                                        .placeholder(R.drawable.profile_img)
                                        .into(holder.profileImageView)

                                }
                            })
                    }
                }
            }
        }

        // set the recycler view
        messageRecyclerView.layoutManager = mLinearLayoutManager
        messageRecyclerView.adapter = mFireBaseAdapter
        sendButton.setOnClickListener {
            if (!intent.extras!!.get("name").toString().equals("")) {
                var targetUserDisplayName = intent.extras!!.get("name")
                var targetUserID = intent.extras!!.get("userId")
                var mCurrentUserId = mFireBaseUser!!.uid
                var currentUserDisplayName : String="NO USER"

                mFireBaseDatabaseRef!!.child("Users")
                    .child(mCurrentUserId).addValueEventListener(object: ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            Log.d("Firebase Error", p0.message)
                        }

                        override fun onDataChange(snapShot: DataSnapshot) {
                            currentUserDisplayName = snapShot.child("display name").value.toString()

                            var sortedUIDs = (userID+mCurrentUserId).toCharArray().sorted()

                            var friendlyMessage = FriendlyMessage(mCurrentUserId,
                                String().returnJoinString(userID,mCurrentUserId),
                                messageEdt.text.toString().trim(),
                                currentUserDisplayName.toString().trim(),
                                targetUserDisplayName.toString().trim())

                            mFireBaseDatabaseRef!!.child("messages").child(targetUserID.toString())
                                .push().setValue(friendlyMessage)

                            mFireBaseDatabaseRef!!.child("messages").child(mCurrentUserId.toString())
                                .push().setValue(friendlyMessage)


                            messageEdt.setText("")
                        }
                    })
            }
        }
    }

    fun String.returnJoinString(a : String, b : String) : String {
        if (a>b)
        {
            return a+b
        }
        else{
            return b+a
        }
    }
    class MessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var messageTextView : TextView?= null
        var messengerTextView : TextView?=null
        var profileImageView : CircleImageView? = null
        var profileImageViewRight: CircleImageView?= null

        fun bindView(friendlyMessage: FriendlyMessage){
            messageTextView = itemView.findViewById<TextView>(R.id.messageTextView)
            messengerTextView = itemView.findViewById<TextView>(R.id.nameofMessanger)
            profileImageView = itemView.findViewById<CircleImageView>(R.id.messengerImageView)
            profileImageViewRight = itemView.findViewById<CircleImageView>(R.id.messengerImageViewRight)

            messageTextView!!.text = friendlyMessage.text.toString()
            messengerTextView!!.text = friendlyMessage.from.toString()
        }
    }

    override fun onStart() {
        super.onStart()
        mFireBaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFireBaseAdapter.stopListening()
    }

}
