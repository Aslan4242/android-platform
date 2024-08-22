package com.example.androidplatform.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.androidplatform.R

object NotificationManager {
    private const val CHANNEL_ID = "channel_id"
    private const val CHANNEL_NAME = "channel_name"

    fun showNotification(
        title: String,
        message: String,
        context: Context,
        id: Int = 0
    ) {
        val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setLargeIcon(
                BitmapFactory.decodeResource(context.resources, R.drawable.notification_large_icon)
            )
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(message)
            .build()

        notificationManager.notify(id, notification)
    }
}
