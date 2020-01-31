package com.almitasoft.choremeapp.ui.notifications

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.*
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NotificationAdapter(
    var notificationList: kotlin.collections.ArrayList<Notification>,
    val notificationsFragment: NotificationsFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemViewType(position: Int): Int {

        when (notificationList[position].notificationType){
            NotificationType.ADDFRIEND -> {return 0}
            NotificationType.NEWFRIENDNOTIFICATION -> {return 1}
            else -> {return 0}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.friend_add_notification,
                parent,false)
            return FriendAddedViewHolder(view)
        }
        else if (viewType==1){
            var view = LayoutInflater.from(parent.context).inflate(R.layout.new_friend_notification,
                parent,false)
            return NewFriendNotificationViewHolder(view)
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
            1->{
                var friendAddedVH = holder as NewFriendNotificationViewHolder
                var notification = notificationList[position] as GeneralNotification
                friendAddedVH.bindItem(notification, position)

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


            val user = User(friendNotification.sourceUName,friendNotification.sourceUID)

            notificationsFragment.notificationsViewModel.getTargetUserData(user).
                observe(notificationsFragment, Observer {
                    it?.let{
                        Picasso.get().load(it.thumb_image_url)
                            .placeholder(R.drawable.default_avata)
                            .into(image)
                    }
                })

            btnApprove.setOnClickListener {
                notificationsFragment.addFriend(friendNotification)
                notificationsFragment.deleteFriendNotification(friendNotification)

                var sourceFriendNotification = AddFriendNotification("",
                    "EMPTY")

                sourceFriendNotification.targetUID = friendNotification.sourceUID
                sourceFriendNotification.targetUName = friendNotification.sourceUName
                sourceFriendNotification.sourceUID = friendNotification.targetUID
                sourceFriendNotification.sourceUName = friendNotification.targetUName

                sourceFriendNotification.notificationThumbImgDestination = friendNotification.notificationThumbImgDestination
                sourceFriendNotification.notificationThumbImgSource = friendNotification.notificationThumbImgSource

                notificationsFragment.deleteFriendNotification(sourceFriendNotification)

                val notificationMessage = "${friendNotification.targetUName} is Your Friend Now!"
                var friendAddedNotification = GeneralNotification(notificationMessage,
                    "EMPTY",NotificationType.NEWFRIENDNOTIFICATION)

                friendAddedNotification.sourceUID = friendNotification.sourceUID
                friendAddedNotification.sourceUName = friendNotification.sourceUName
                friendAddedNotification.targetUID = friendNotification.targetUID
                friendAddedNotification.targetUName = friendNotification.targetUName

                friendAddedNotification.notificationThumbImgDestination = friendNotification.notificationThumbImgDestination
                friendAddedNotification.notificationThumbImgSource = friendNotification.notificationThumbImgSource

                notificationsFragment.addGeneralNotification(friendAddedNotification)

                notificationsFragment.addPushNotification(friendAddedNotification)

            }

            btnReject.setOnClickListener {
                notificationsFragment.deleteFriendNotification(friendNotification)

                var sourceFriendNotification = AddFriendNotification("",
                    "EMPTY")

                sourceFriendNotification.targetUID = friendNotification.sourceUID
                sourceFriendNotification.targetUName = friendNotification.sourceUName
                sourceFriendNotification.sourceUID = friendNotification.targetUID
                sourceFriendNotification.sourceUName = friendNotification.targetUName

                sourceFriendNotification.notificationThumbImgDestination = friendNotification.notificationThumbImgDestination
                sourceFriendNotification.notificationThumbImgSource = friendNotification.notificationThumbImgSource

                notificationsFragment.deleteFriendNotification(sourceFriendNotification)

                var friendsRejected = GeneralNotification(
                    "${friendNotification.targetUName} Has Rejected Your Friend Request :(",
                    "EMPTY",NotificationType.NEWFRIENDNOTIFICATION)

                friendsRejected.sourceUID = friendNotification.sourceUID
                friendsRejected.targetUID = friendNotification.targetUID
                friendsRejected.sourceUName = friendNotification.sourceUName
                friendsRejected.targetUName = friendNotification.targetUName
                friendsRejected.notificationThumbImgSource =friendNotification.notificationThumbImgSource
                friendsRejected.notificationThumbImgDestination = friendNotification.notificationThumbImgDestination

                notificationsFragment.addGeneralNotification(friendsRejected)
                notificationsFragment.addPushNotification(friendsRejected)
            }
        }
    }

    inner class NewFriendNotificationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(generalNotification: GeneralNotification, position : Int){
            val tvUserAdded = itemView.findViewById<TextView>(R.id.tvNewFriendAdded)
            val btnReject = itemView.findViewById<Button>(R.id.btnRemoveMessage)
            val image = itemView.findViewById<ImageView>(R.id.IVNewFriend)

            image.setImageResource(R.drawable.default_avata)

            tvUserAdded.text = generalNotification.notificationMessage

            Picasso.get().load(generalNotification.notificationThumbImgDestination)
                .placeholder(R.drawable.default_avata)
                .into(image)

            btnReject.setOnClickListener {
                Log.d("User Event","NotificationAdapter. User Clicked On Delete")
                notificationsFragment.removeGeneralNotification(generalNotification)

                //notificationsFragment.deleteFriendNotification(friendNotification)
            }
        }
    }



}