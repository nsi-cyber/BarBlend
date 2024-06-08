package com.nsicyber.barblend.data.repository

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nsicyber.barblend.R
import com.nsicyber.barblend.domain.repository.NotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NotificationRepository {

    private val channelId = "notification_channel"

    init {
        createNotificationChannel()
    }

    override fun pushNotification() {
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Title")
            .setContentText("This is a notification")
            .setSmallIcon(R.drawable.ic_menu_explore)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        Log.d("pushNotification(): ", "Notification")
        NotificationManagerCompat.from(context).notify(1, notification)
    }

    private fun createNotificationChannel() {
        val name = "Notification Channel"
        val descriptionText = "This is a notification channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}