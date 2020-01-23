package com.almitasoft.choremeapp.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.AddFriendNotification
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.model.NotificationType
import com.almitasoft.choremeapp.model.User
import com.almitasoft.choremeapp.ui.MainActivity
import com.almitasoft.choremeapp.ui.SharedViewModel
import kotlinx.android.synthetic.main.fragment_notifications.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.get

class NotificationsFragment : Fragment() {

    private lateinit var notificationAdapter: NotificationAdapter
    lateinit var sharedViewModel: SharedViewModel
    val notificationsViewModel : NotificationsViewModel by viewModel()
    private lateinit var mainActivity : MainActivity
    var friendsList = arrayListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        notificationsViewModel =
//            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)


        activity?.let{
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            mainActivity = activity as MainActivity
        }


        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        return root
    }

    fun setAdapter(){
        notificationAdapter = NotificationAdapter(arrayListOf(),this)


        notificationRV.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        notificationAdapter.notifyDataSetChanged()
    }

    fun observe(){

        notificationsViewModel.refreshData()

        notificationsViewModel.notificationList.observe(this, Observer {
            notificationAdapter.notificationList.addAll(it)
            notificationAdapter.notifyDataSetChanged()
        })

        sharedViewModel.getFriendsList().observe(this,object: Observer<ArrayList<User>>{
            override fun onChanged(friends: ArrayList<User>?) {
                friends?.let{
                    friendsList = it
                }
            }
        })

//        notificationsViewModel.getNotificationsList().observe(this, Observer {
//            notificationAdapter.notificationList.addAll(it)
//            notificationAdapter.notifyDataSetChanged()
//        })

    }

    fun deleteFriendNotification(notification: AddFriendNotification){

        notificationsViewModel.deleteFriendNotification(notification).observe(this, Observer {
            if (it.result == "OK"){
                Toast.makeText(activity!!.applicationContext,
                    "User ${notification.sourceUName} Notification was deleted !",
                    Toast.LENGTH_LONG).show()
                notificationAdapter.notificationList.remove(notification)
                notificationAdapter.notifyDataSetChanged()
                Log.d("Delete Notifiation Success", "User ${notification.sourceUName} Notification was deleted !")
            }else{
                Log.d("NotificationsFragment:deleteFriend", "Error = ${it.result}")
            }
        })
    }

    fun addFriend(notification : AddFriendNotification){
        val user = User(notification.sourceUName, notification.sourceUID)

        sharedViewModel.getTargetUserData(user).observe(this, Observer {returnedUser->

            returnedUser?.let{
                sharedViewModel.addFriend(it).observe(this, Observer {
                    if (it.result == "OK"){
                        Toast.makeText(activity!!.applicationContext,
                            "User ${user.displayName} is Your Friend Now!",
                            Toast.LENGTH_SHORT).show()
                        notificationAdapter.notificationList.remove(notification)
                        notificationAdapter.notifyDataSetChanged()
                        mainActivity.navController.navigate(R.id.navigation_notifications)
                    }
                    else{
                        Log.d("NotificationsFragment:AddFriend", "Error = ${it.result.toString()}")
                    }
                })

                sharedViewModel.getFriendsList().observe(this, object: Observer<ArrayList<User>> {

                    override fun onChanged(t: ArrayList<User>?) {
                        CurrentUser.friendsList = t
                    }
                })
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setAdapter()
        observe()
    }
}