package com.almitasoft.choremeapp.Notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class NotificationsReceiver : BroadcastReceiver(), KoinComponent{
    private val notificationService :  NotificationSender by inject { parametersOf(this) }

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"Received a notification", Toast.LENGTH_LONG).show()

        if (intent!!.action.equals("com.tester.close")){
            Log.d("Close Broadcast","Close event received in service")
            notificationService.removeNotificationChannel()
            notificationService.createNotificationChannel()
        }
    }
}
