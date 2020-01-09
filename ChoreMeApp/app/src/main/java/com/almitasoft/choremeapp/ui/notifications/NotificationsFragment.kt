package com.almitasoft.choremeapp.ui.notifications

import android.app.Activity
import android.app.Notification
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.AddFriendNotification
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var notificationAdapter: NotificationAdapter
    var notificationsList = arrayListOf<com.almitasoft.choremeapp.model.Notification>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        notificationsViewModel.text.observe(this, Observer {

        })
        return root
    }

    fun setAdapter(){
        notificationAdapter = NotificationAdapter(notificationsList)

        notificationsList.add(AddFriendNotification("XXX Wants to add you"))
        notificationsList.add(AddFriendNotification("YYY Wants to add you"))
        notificationRV.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        notificationAdapter.notifyDataSetChanged()
    }

    fun observe(){
        notificationsViewModel.getUsersList().observe(this, Observer {
            notificationsList.addAll(it)
            notificationAdapter.notifyDataSetChanged()
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setAdapter()
        observe()
    }
}