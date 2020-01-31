package com.almitasoft.choremeapp.Notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.almitasoft.choremeapp.ui.MainActivity
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf


class NotificationsReceiver : BroadcastReceiver(), KoinComponent{
    private val notificationService :  NotificationSender by inject { parametersOf(this) }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals("com.tester.close")){
            Log.d("NotificationsReceiver::onReceive","Close event received in service")
            notificationService.closeNotification()
            notificationService.removeNotificationChannel()
            notificationService.createNotificationChannel()
        }

        if (intent.action.equals("com.tester.open")){
            //start activity
            //start activity
            Log.d("NotificationsReceiver::onReceive","com.tester.open event received in service")
            notificationService.closeNotification()
            val i = Intent()
            i.setClassName(context!!.packageName, MainActivity::class.java.name)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }
}
