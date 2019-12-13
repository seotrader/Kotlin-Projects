package com.almitasoft.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.util.*

class SaveData {

    var context: Context
    var sharedRef:SharedPreferences?= null
    val snoozeTimeOut = 1000*60*5
    var alarmMode: Boolean = false

    constructor(context: Context){
        this.context = context
        sharedRef = context.getSharedPreferences("myref", Context.MODE_PRIVATE)
        alarmMode = sharedRef!!.getBoolean("mode", false)
    }

    fun turnOff(){
        alarmMode = false
        var editor = sharedRef!!.edit()
        editor.putBoolean("mode",alarmMode)
        editor.commit()

    }
    fun turnOn(){
        alarmMode = true
        var editor = sharedRef!!.edit()
        editor.putBoolean("mode",alarmMode)
        editor.commit()
    }

    fun save(hour: Int, minute:Int){
        var editor = sharedRef!!.edit()
        editor.putInt("hour",hour)
        editor.putInt("minute",minute)
        editor.putBoolean("mode",alarmMode)
        editor.commit()
    }

    fun getHour() : Int{
        return sharedRef!!.getInt("hour", 0)
    }

    fun getMinute(): Int{
        return sharedRef!!.getInt("minute", 0)
    }

    fun setSnooze()
    {
        val calendar = Calendar.getInstance()

        // only run on the current day
        calendar.set(Calendar.HOUR_OF_DAY,getHour())
        calendar.set(Calendar.MINUTE,getMinute())
        calendar.set(Calendar.SECOND,0)




        calendar.timeInMillis += snoozeTimeOut

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intnet = Intent(context, myBroadCastReceiver::class.java)
        intnet.putExtra("message","Alarm Time ${getHour()}:${getMinute()}!!!")
        intnet.action = "com.tester.alarmmanager"

        val pi = PendingIntent.getBroadcast(context,0
            , intnet, PendingIntent.FLAG_UPDATE_CURRENT)

        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,pi)
    }

    fun stopAlarm(){

    }

    fun removeAlarm(){
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intnet = Intent(context, myBroadCastReceiver::class.java)

        intnet.action = "com.tester.alarmmanager"

        val pi = PendingIntent.getBroadcast(context,1,
            intnet, PendingIntent.FLAG_UPDATE_CURRENT)

        am.cancel(pi)
    }

    fun setAlarm(hour: Int, minute: Int){

        val calendar = Calendar.getInstance()
        var setForNextDay = false

        if ( (hour<calendar.get(Calendar.HOUR_OF_DAY)) )
            setForNextDay = true

        if ( (hour==calendar.get(Calendar.HOUR_OF_DAY))
            && minute<=calendar.get(Calendar.MINUTE)){
            setForNextDay = true
        }

        // only run on the current day
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)
        calendar.set(Calendar.SECOND,0)

        if (setForNextDay==true){
            calendar.add(Calendar.DAY_OF_YEAR, 1)

            if (calendar.get(Calendar.DAY_OF_YEAR)==365){
                calendar.add(Calendar.YEAR, 1)
            }
        }

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intnet = Intent(context, myBroadCastReceiver::class.java)
        intnet.action = "com.tester.alarmmanager"
        intnet.putExtra("message","Alarm Time ${hour}:${minute}!!!")

        val pi = PendingIntent.getBroadcast(context,1,
            intnet, PendingIntent.FLAG_UPDATE_CURRENT)

        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,pi)
    }
}