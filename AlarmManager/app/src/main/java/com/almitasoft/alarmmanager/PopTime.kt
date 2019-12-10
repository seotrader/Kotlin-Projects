package com.almitasoft.alarmmanager

import android.os.Build
import android.os.Bundle
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.zip.Inflater

class PopTime : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var myView = inflater.inflate(R.layout.pop_time,container, false)

        var buDone = myView.findViewById<Button>(R.id.doneBtn)
        var tp1 = myView.findViewById<TimePicker>(R.id.tp1)

        buDone.setOnClickListener {
            val ma = activity as MainActivity

            if (Build.VERSION.SDK_INT>=23) {
                ma.SetTime(tp1.hour, tp1.minute)
            }
            else{
                ma.SetTime(tp1.currentHour, tp1.currentMinute)
            }
            dismiss()
        }

        return myView
    }

    fun setTime(hours : Int, minutes : Int){
        showTimeTV.text = "$hours : $minutes"


    }
}