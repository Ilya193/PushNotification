package ru.xlwe.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("attadag", "TEST")
        if (message.notification != null) {
            try {
                createNotification(message.notification!!.title!!, message.notification!!.body!!)
            } catch (e: Exception) {

            }
        }
    }

    private fun createNotificationChannel(): NotificationManager {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return notificationManager
    }

    private fun createNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setVibrate(longArrayOf(1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setContent(createView(title, message))
            .build()

        val notificationManager = createNotificationChannel()
        notificationManager.notify(0, notification)
    }

    private fun createView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("ru.xlwe.pushnotification", R.layout.push)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.text, message)
        remoteView.setImageViewResource(R.id.logo, R.drawable.ic_message)
        return remoteView
    }

    private companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
    }
}