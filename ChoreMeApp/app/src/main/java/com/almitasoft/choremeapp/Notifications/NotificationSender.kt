package com.almitasoft.choremeapp.Notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.almitasoft.choremeapp.R


class NotificationSender(var context:Context) {
    val NOTIFIYTAG="New ChoreMe Notification"
    val NOTIFICATION_CHANNEL = "notification_channel_02"

    fun closeNotification(){
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         notificationManager.cancel(NOTIFIYTAG,0)
    }
    fun removeNotificationChannel()
    {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL)
        }
    }
    fun createNotificationChannel(){

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NOTIFICATION_CHANNEL
            val descriptionText = "ChoreMe Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL)

            if (!notificationManager.areNotificationsEnabled())
            {
                Toast.makeText(context, "Please Enable Notifications !"
                    , Toast.LENGTH_LONG).show()
                openNotificationSettings(context)
            }

            val alarmSound =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

            val channel = NotificationChannel(NOTIFICATION_CHANNEL, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400,
                    100, 200, 300, 400, 500, 400, 300, 200, 400)
                setSound(alarmSound,att)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun openNotificationSettings(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            ContextCompat.startActivity(context, intent, null)
        }else{
            var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.setData(Uri.parse("package:"+context.packageName))
            ContextCompat.startActivity(context, intent, null)
        }
    }
    fun Notify(message:String,number:Int){

        var closeIntent = Intent(context, NotificationsReceiver::class.java)
        closeIntent.action = "com.tester.close"
        closeIntent.putExtra("message","Friend Notification")

        var closePendingIntent = PendingIntent.getBroadcast(context, 0,
            closeIntent, 0)

        var openAppIntent = Intent(context, NotificationsReceiver::class.java)
        openAppIntent.action = "com.tester.open"
        var openPendingIntent = PendingIntent.getBroadcast(context, 0,
            openAppIntent, 0)

        val builder= NotificationCompat.Builder(context,NOTIFICATION_CHANNEL)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentTitle("Friend Notification")
            //.setCustomContentView(collapesedView)
            //.setCustomBigContentView(expandedView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentText(message)
            .setNumber(number)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(
                R.drawable.ic_launcher_background,
                "Close",
                closePendingIntent )
            .addAction(
                R.drawable.ic_launcher_background,
                "Goto App",
                openPendingIntent )
            .setSmallIcon(R.drawable.ic_minus_white)

            .setLargeIcon(
                BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_avata))
            .setAutoCancel(true)
            .setColor( ContextCompat.getColor(context, R.color.colorPrimary))

        val nm=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var mNotification: Notification = builder.build()


        val oldVersionBuilder = NotificationCompat.Builder(context)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentTitle("New request")
            .setContentText(message)
            .setNumber(number)
            .addAction(
                R.drawable.ic_launcher_background,
                "Close",
                closePendingIntent )
            .addAction(
                R.drawable.ic_launcher_background,
                "Goto App",
                openPendingIntent )
            .setSmallIcon(R.drawable.notification_icon_background)
            .setAutoCancel(true)
        //mNotification.flags = Notification.FLAG_INSISTENT

//        with(NotificationManagerCompat.from(context)) {
//            // notificationId is a unique int for each notification that you must define
//            notify(1, builder.build())
//        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFIYTAG, 0,mNotification)
        }else{
            nm.notify(NOTIFIYTAG.hashCode(), oldVersionBuilder.build())
        }

    }
}