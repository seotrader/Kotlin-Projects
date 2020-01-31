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
import com.almitasoft.choremeapp.model.*
import com.almitasoft.choremeapp.ui.MainActivity
import com.almitasoft.choremeapp.ui.SharedViewModel
import kotlinx.android.synthetic.main.fragment_notifications.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationsFragment : Fragment() {

    private lateinit var notificationAdapter: NotificationAdapter
    private val sharedViewModel: SharedViewModel by sharedViewModel()
    val notificationsViewModel : NotificationsViewModel by viewModel()
    private lateinit var mainActivity : MainActivity
    var friendsList = arrayListOf<User>()
    var activityFriendsList = arrayListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        notificationsViewModel =
//            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)

        activity?.let{
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
            notificationAdapter.notificationList.clear()
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
    }

    fun removeGeneralNotification(notification: GeneralNotification){
        notificationsViewModel.removeGeneralNotification(notification).observe(this, Observer {
            if (it.result == "OK"){
                notificationAdapter.notificationList.remove(notification)
                notificationAdapter.notifyDataSetChanged()
                Log.d("Info", "General Notification ${notification.sourceUName} Notification was deleted !")
            }else{
                Log.d("Error", "Error = ${it.result}")
            }
        })

    }

    fun deleteFriendNotification(notification: AddFriendNotification){

        notificationsViewModel.deleteFriendNotification(notification).observe(this, Observer {
            if (it.result == "OK"){
                notificationAdapter.notificationList.remove(notification)
                notificationAdapter.notifyDataSetChanged()
                Log.d("Info", "User ${notification.sourceUName} Notification was deleted !")
            }else{
                Log.d("Error", "Error = ${it.result}")
            }
        })
    }

    fun addPushNotification(notification: GeneralNotification){
        notificationsViewModel.addPushNotification(notification).observe(this, Observer {
            if (it.result == "OK"){
                Log.d("Info","NotificationFragment:addGeneralNotification. , SUCCESS")
            }
            else{
                Log.d("Error","NotificationFragment:addGeneralNotification, Error = ${it.result}")
            }

        })

    }
    fun addGeneralNotification(notification: GeneralNotification){
       notificationsViewModel.addGeneralNotification(notification).observe(this, Observer {
           if (it.result == "OK"){
               Log.d("Info","NotificationFragment:addGeneralNotification. , SUCCESS")
           }
           else{
               Log.d("Error","NotificationFragment:addGeneralNotification, Error = ${it.result}")
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