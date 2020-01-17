package com.almitasoft.choremeapp.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.AddFriendNotification
import com.almitasoft.choremeapp.model.Notification
import com.almitasoft.choremeapp.model.User

class NotificationAdapter(
    var notificationList: kotlin.collections.ArrayList<Notification>,
    val notificationsFragment: NotificationsFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {

        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.friend_add_notification,
                parent,false)
            return FriendAddedViewHolder(view)
        }
        else{
            var view = LayoutInflater.from(parent.context).inflate(R.layout.friend_add_notification,
                parent,false)
            return FriendAddedViewHolder(view)
        }
    }

    override fun getItemCount() = notificationList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType){
            0 -> {
                var friendAddedVH = holder as FriendAddedViewHolder
                var notification = notificationList[position] as AddFriendNotification
                friendAddedVH.bindItem(notification)
            }
        }
    }

    inner class FriendAddedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(friendNotification: AddFriendNotification){
            val tvUserAdded = itemView.findViewById<TextView>(R.id.tvUserAdded)
            val btnApprove = itemView.findViewById<Button>(R.id.btnApprove)
            val btnReject = itemView.findViewById<Button>(R.id.btnReject)

            val image = itemView.findViewById<ImageView>(R.id.IVFriend)

            image.setImageResource(R.drawable.default_avata)

            tvUserAdded.text = friendNotification.notificationMessage

            btnApprove.setOnClickListener {
                notificationsFragment.addFriend(friendNotification)
            }

            btnReject.setOnClickListener {
                val user = User(friendNotification.sourceUName,friendNotification.sourceUID)
                notificationsFragment.deleteFriend(user,friendNotification)
            }
        }
    }

}