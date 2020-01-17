package com.almitasoft.choremeapp.ui.addusers

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
import com.almitasoft.choremeapp.Notifications.NotificationSender
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.model.AddFriendNotification
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.model.User
import com.almitasoft.choremeapp.ui.MainActivity
import com.almitasoft.choremeapp.ui.SharedViewModel
import kotlinx.android.synthetic.main.search_users_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.inject
import org.koin.core.parameter.parametersOf


class SearchUsers : Fragment() {

    private lateinit var mainActivity: MainActivity
    lateinit var searchUsersAdapter : SearchUsersAdapter
    var usersArrayList = arrayListOf<User>()
    private val fbManager : FireBaseManager by inject()
    private val notificationService : NotificationSender by inject { parametersOf(this) }

    var notificationsList = arrayListOf<com.almitasoft.choremeapp.model.Notification>()

    companion object {
        fun newInstance() = SearchUsers()
    }

    private lateinit var viewModel: SearchUsersViewModel
    private lateinit var sharedViewModel : SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_users_fragment, container, false)
    }

    fun observers(){
        sharedViewModel.updatedFilteredText.observe(this, Observer {
            searchUsersAdapter.filter.filter(it)
        })

        viewModel.loadingUsers.observe(this, Observer {
            if (it==true){
                progressBarSearchUsers.visibility = View.VISIBLE
            }
            else{
                progressBarSearchUsers.visibility = View.GONE
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.mainActivity = activity as MainActivity

        viewModel = ViewModelProviders.of(this).get(SearchUsersViewModel::class.java)
        sharedViewModel = ViewModelProviders.of(mainActivity).get(SharedViewModel::class.java)

        searchUsersAdapter = SearchUsersAdapter(usersArrayList,this)

        RVSearchUsers.apply {
            adapter = searchUsersAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        // if user isn't connected, send to welcome screen
        if (!viewModel.isUserConnected()){
            Toast.makeText(mainActivity, "Login First Or Create An Account!",
                Toast.LENGTH_SHORT).show()
            mainActivity.navController.navigate(R.id.welcome_screen)

        }

        if (viewModel.isUserConnected()){

            viewModel.getListOfUsers().observe(this, Observer{listReceived->
                Toast.makeText(mainActivity, "User List Was Read Successfully",
                    Toast.LENGTH_SHORT).show()

                usersArrayList.clear()

                viewModel.getNotificationList().observe(this, Observer { notificationList->
                    notificationList.removeIf {
                        it.sourceUID == CurrentUser.userID
                    }
                })

                listReceived.removeIf {
                    it.userID == CurrentUser.userID
                }

                CurrentUser.friendsList!!.forEach { user->
                    listReceived.removeIf {
                        user.userID == it.userID
                    }
                }

                listReceived.forEach {
                    usersArrayList.add(it)
                    searchUsersAdapter.savedFullUserList.add(it)
                    searchUsersAdapter.usersList = usersArrayList

                }
                searchUsersAdapter.notifyDataSetChanged()
            })
        }

        observers()
    }

    fun addUser(user : User){
        fbManager.addFriendRequest(user).observe(this, Observer {
            if (it.result == "OK"){
                usersArrayList.remove(user)
                searchUsersAdapter.notifyDataSetChanged()
            }
        })

        var notification = AddFriendNotification("New Friend request From ${CurrentUser.displanyName}","EMPTY")
        notification.sourceUID = CurrentUser.userID!!
        notification.sourceUName = CurrentUser.displanyName!!
        notification.status = "WAITING"
        notification.targetUID = user.userID
        notification.targetUName = user.displayName

        viewModel.addFriendNotification(notification).observe(this, Observer {
            if (it.result == "OK"){
                Log.d("SearchUser:AddUser","Notification Added")
            }
            else{
                Log.d("Error SearchUser:AddUser","Error = ${it.result}")
            }
        })
    }

    override fun onResume() {
        val actionBar = mainActivity.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        super.onResume()
    }
}
