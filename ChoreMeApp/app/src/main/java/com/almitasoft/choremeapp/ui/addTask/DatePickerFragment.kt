package com.almitasoft.choremeapp.ui.addTask

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.ui.MainActivity
import kotlinx.android.synthetic.main.add_task_fragment.*
import java.text.DateFormat
import java.util.*

interface PickDate{
    fun onDatePicked(calendar: Calendar)
}

class DatePickerFragment(var datePicked : PickDate) : DialogFragment(), DatePickerDialog.OnDateSetListener{

    lateinit var mainActivity : MainActivity

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mainActivity = activity as MainActivity

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(this.context!!, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        Log.d("info","Date picker = $day - $month - $year")

        val calendar = Calendar.getInstance()

        // only run on the current day
        calendar.set(Calendar.HOUR_OF_DAY,23)
        calendar.set(Calendar.MINUTE,59)
        calendar.set(Calendar.SECOND,50)
        calendar.set(Calendar.DAY_OF_MONTH,day)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.YEAR,year)

        datePicked.onDatePicked(calendar)



    }

}