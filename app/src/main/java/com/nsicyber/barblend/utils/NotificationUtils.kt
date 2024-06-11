package com.nsicyber.barblend.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nsicyber.barblend.MainActivity
import com.nsicyber.barblend.R
import com.nsicyber.barblend.common.Constants

object NotificationUtils {
    fun pushNotification(context: Context) {
        val intent =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification =
            NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
                .setContentTitle("Great News!")
                .setContentText("There are new cocktails in the app")
                .setSmallIcon(R.drawable.ic_menu_explore)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        Log.d("pushNotification(): ", "Notification")
        NotificationManagerCompat.from(context).notify(1, notification)
    }

    fun createNotificationChannel(context: Context) {
        val name = "BarBlendChannel"
        val descriptionText = "This is the BarBlend notification channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel(Constants.NOTIFICATION_CHANNEL, name, importance).apply {
                description = descriptionText
            }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
