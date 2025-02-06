package com.jhomlala.better_player_example

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN

class BetterPlayerService : Service() {

    companion object {
        const val notificationId = 20772077
        const val foregroundNotificationId = 20772078
        const val channelId = "VideoPlayer"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

         // Check permission at runtime for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission for foreground service media playback not granted", Toast.LENGTH_SHORT).show()
                stopSelf() // Stop the service if permission is not granted
                return START_NOT_STICKY
            }
        }

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(channelId, "Channel")
            } else {
                ""
            }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )


        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Better Player Notification")
            .setContentText("Better Player is running")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setOngoing(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setCategory(Notification.CATEGORY_SERVICE);
        }
        startForeground(foregroundNotificationId, notificationBuilder.build())
        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        try {
            val notificationManager =
                getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager
            notificationManager.cancel(notificationId)
        } catch (exception: Exception) {
exception.printStackTrace() 
        } finally {
            stopSelf()
        }
    }

}