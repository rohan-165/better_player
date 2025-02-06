package com.jhomlala.better_player_example

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity

class MainActivity : FlutterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startNotificationService()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopNotificationService()
    }

    ///TODO: Call this method via channel after remote notification start
    private fun startNotificationService() {
        try {
            val intent = Intent(this, BetterPlayerService::class.java)
              if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                // Check if permission is granted before starting the foreground service
                if (checkSelfPermission(android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK) == PackageManager.PERMISSION_GRANTED) {
                    startForegroundService(intent)
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
                }
            } else {
                startService(intent)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    ///TODO: Call this method via channel after remote notification stop
    private fun stopNotificationService() {
        try {
            val intent = Intent(this, BetterPlayerService::class.java)
            stopService(intent)
        } catch (exception: Exception) {
            exception.printStackTrace()

        }
    }
}
