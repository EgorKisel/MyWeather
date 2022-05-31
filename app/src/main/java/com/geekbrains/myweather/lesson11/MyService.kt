package com.geekbrains.myweather.lesson11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.geekbrains.myweather.R
import com.geekbrains.myweather.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("mylogs_push", "$message")
        if (!message.data.isNullOrEmpty()){
            val title = message.data[KEY_TITLE]
            val message = message.data[KEY_MESSAGE]
            if (!title.isNullOrEmpty()&&!message.isNullOrEmpty()){
                push(title, message)
            }
        }
    }

    companion object{
        private const val NOTIFICATION_LOW_ID = 1
        private const val NOTIFICATION_HIGH_ID = 2
        private const val CHANNEL_ID_LOW = "channel_id_1"
        private const val CHANNEL_ID_HIGH = "channel_id_2"

        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"
    }

    private fun push(title: String, message: String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilderLow = NotificationCompat.Builder(this, CHANNEL_ID_LOW).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle("TITLE LOW")
            setContentText("TEXT LOW")
            priority = NotificationManager.IMPORTANCE_LOW
        }
        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_ID_HIGH).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(title)
            setContentText(message)
            setContentIntent(contentIntent)
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channelNameLow = "Name ${CHANNEL_ID_LOW}"
            val channelDescriptionLow = "Description ${CHANNEL_ID_LOW}"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow = NotificationChannel(CHANNEL_ID_LOW, channelNameLow, channelPriorityLow).apply {
                description = channelDescriptionLow
            }
            notificationManager.createNotificationChannel(channelLow)
        }
        notificationManager.notify(NOTIFICATION_LOW_ID, notificationBuilderLow.build())

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channelNameHigh = "Name ${CHANNEL_ID_HIGH}"
            val channelDescriptionHigh = "Description ${CHANNEL_ID_HIGH}"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh = NotificationChannel(CHANNEL_ID_HIGH, channelNameHigh, channelPriorityHigh).apply {
                description = channelDescriptionHigh
            }
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_HIGH_ID, notificationBuilderHigh.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}