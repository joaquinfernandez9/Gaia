package com.example.uigaiav2.domain.usecases.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.uigaiav2.R


class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val taskName = intent?.getStringExtra("taskName")


        println("Alarm triggered: $taskName")

        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "AlarmChannelId"
        val channelName = "Alarm Channel"

        // Crear el canal de notificación
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        // Crear la notificación
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Your time is up!")
            .setContentText("Task: $taskName, is due!")
            .setSmallIcon(R.drawable.tree_ico)
            .build()

        // Mostrar la notificación
        notificationManager.notify(1, notification)
    }


}