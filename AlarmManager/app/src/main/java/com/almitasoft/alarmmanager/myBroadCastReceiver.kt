package com.almitasoft.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class myBroadCastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        var b = intent!!.extras?.run{
            this.get("message")
        }
        var appNotify = Notification(context!!)


        if (intent.action.equals("com.tester.alarmmanager")) {
            Toast.makeText(context," ${b.toString()}", Toast.LENGTH_LONG).show()
            val notifyMe = Notification(context)
            notifyMe.Notify(context,b.toString(),10)
        }else if (intent.action.equals("android.intent.action.BOOT_COMPLETED")){
            val saveData = SaveData(context)
            saveData.setAlarm(saveData.getHour(),saveData.getMinute())
        }else if (intent.action.equals("com.tester.close")){
            appNotify.removeNotification()
            appNotify.createNotificationChannel(context)
        }
        }
    }