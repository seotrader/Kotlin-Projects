package com.almitasoft.choremeapp.ui.addTask

import android.app.DatePickerDialog
import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment

import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.ui.MainActivity
import com.almitasoft.choremeapp.ui.notifications.NotificationsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_task_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.util.*

class AddTaskFragment : Fragment() {

    val addTaskViewModel : NotificationsViewModel by viewModel()
    lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private lateinit var viewModel: AddTaskViewModel

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
            val newFragment = DatePickerFragment(this)
            newFragment.show(this.fragmentManager!!, "datePicker")

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
    }


}
