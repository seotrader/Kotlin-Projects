package com.almitasoft.choremeapp.ui.addTask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.model.User
import com.almitasoft.choremeapp.ui.MainActivity
import com.almitasoft.choremeapp.ui.SharedViewModel
import com.almitasoft.choremeapp.ui.notifications.NotificationsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_task_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.util.*


class AddTaskFragment : Fragment() {

    val addTaskViewModel : AddTaskViewModel by viewModel()
    val sharedViewModel: SharedViewModel by sharedViewModel()

    lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private lateinit var viewModel: AddTaskViewModel

    override fun onPause() {
        mainActivity.fab.show()
        super.onPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_task_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainActivity = activity as MainActivity

        loadscreen()

        guiObservers()
    }

    override fun onStop() {
        super.onStop()
        mainActivity.fab.show()
    }

    fun guiObservers(){
        btnSelectedDate.setOnClickListener {

            var datePicked = object: PickDate{
                override fun onDatePicked(calendar: Calendar) {
                    TVCurrentDate.text =
                        "Task Date: ${DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)}"

                }
            }
            val newFragment = DatePickerFragment(datePicked)
            newFragment.show(this.fragmentManager!!, "datePicker")
        }

        btnTargetUser.setOnClickListener {
            var selectUserFragment : PickUserDialogFragment?=null

            var userPick = object : UserPicked{
                override fun onUserPicker(user: User) {
                    selectUserFragment?.run{
                        this.dismiss()
                    }


                    addTaskViewModel.userPicket = user
                    TVUserPicked.text =
                        "Current Target User: ${addTaskViewModel.userPicket.displayName}"

                    Log.d("Info","Picked the user ${user.displayName}")

                }
            }

            selectUserFragment = PickUserDialogFragment(userPick)
            selectUserFragment.show(this.fragmentManager!!,"PickUser")
            Log.d("Info","Closed PickUserDialog Fragment")
        }



    }



    fun loadscreen(){

        mainActivity.fab.hide()
        var image = CIVCurrentUser

        Picasso.get().load(CurrentUser.thumb_image_url)
            .placeholder(R.drawable.default_avata)
            .into(image)

        TVUserName.text = CurrentUser.displanyName

        var calendar : Calendar = Calendar.getInstance()

        var currentDate = DateFormat.getDateInstance().format(calendar.time)

        TVCurrentDate.text =
            "Task Date: ${DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)}"

        Log.d("info","Current Date: $currentDate")

        TVUserPicked.text =
            "Current Target User: ${CurrentUser.displanyName}"
    }


}
