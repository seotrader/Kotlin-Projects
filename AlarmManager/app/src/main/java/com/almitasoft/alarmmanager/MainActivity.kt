package com.almitasoft.alarmmanager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.almitasoft.alarmmanager.Notification
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var saveData : SaveData
    var appNotify = Notification(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveData = SaveData(this)
        var min = saveData.getMinute().toString()

        title = "Simple Alarm App"

        if (saveData.getMinute()<10){
            min = "0$min"
        }

        if (saveData.alarmMode == true){
            showTimeTV.text= "Alarm Set To: ${saveData.getHour()}:$min"
        } else{
            showTimeTV.text= "ALARM IS OFF"
        }



        appNotify.createNotificationChannel(this)

        stopAlarmBtn.setOnClickListener {
            saveData.turnOff()
            saveData.removeAlarm()
            appNotify.removeNotification()
            showTimeTV.text= "ALARM IS OFF"
        }
    }

    fun nextAlarmMillis(hour: Int, minute: Int) : Long {

        val calendarCurrent = Calendar.getInstance()
        val calendarAlarm = Calendar.getInstance()

        // only run on the current day
        calendarAlarm.set(Calendar.HOUR_OF_DAY,hour)
        calendarAlarm.set(Calendar.MINUTE,minute)
        calendarAlarm.set(Calendar.SECOND,0)

        return calendarCurrent.timeInMillis - calendarAlarm.timeInMillis
    }

    fun buSetTime(view : View){
        val popTime = PopTime()
        val fm = supportFragmentManager
        popTime.show(fm, "Current Alarm ")
    }

    fun SetTime(hours:Int,minutes:Int){

        var min = minutes.toString()

        if (minutes<10){
            min = "0$min"
        }

        showTimeTV.text= "Alarm Set To: $hours:$min"


        appNotify.createNotificationChannel(this)
        saveData.turnOn()
        saveData.save(hours,minutes)
        saveData.setAlarm(hours,minutes)
    }
}
