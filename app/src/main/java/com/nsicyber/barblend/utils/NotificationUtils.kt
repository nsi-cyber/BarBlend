package com.nsicyber.barblend.utils


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.nsicyber.barblend.R

object NotificationUtils {

    private const val CHANNEL_ID = "CocktailChannel"

    fun sendNotification(context: Context, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, "Cocktail Updates", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("BarBlend")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_menu_explore)
            .build()
        notificationManager.notify(1, notification)
    }
}