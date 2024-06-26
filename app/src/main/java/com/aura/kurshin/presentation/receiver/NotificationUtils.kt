package com.aura.kurshin.presentation.receiver

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

object NotificationUtils {

    private const val CHANNEL_ID = "BootEventsChannel"
    private const val NOTIFICATION_ID = 123
    private const val BOOT_EVENT_SP = "BootEvents"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Boot Events Channel"
            val descriptionText = "Channel for Boot Events"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(BOOT_EVENT_SP, Context.MODE_PRIVATE)
        val bootCount = sharedPreferences.getInt("boot_count", 0)
        val lastBootTime = sharedPreferences.getString("last_boot_time", null)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentTime = dateFormat.format(Date())

        val notificationText = when (bootCount) {
            0 -> "No boots detected"
            1 -> "The boot was detected = $lastBootTime"
            else -> {
                val lastBoot = dateFormat.parse(lastBootTime)
                val preLastBootTime = sharedPreferences.getString("pre_last_boot_time", null)
                val preLastBoot = dateFormat.parse(preLastBootTime)
                val timeDiff = lastBoot.time - preLastBoot.time
                "Last boots time delta = ${timeDiff / (1000 * 60)} minutes"
            }
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Boot Event Notification")
            .setContentText(notificationText)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
            return
        }
    }

    fun isNotificationScheduled(context: Context): Boolean {
        val notificationManager = NotificationManagerCompat.from(context)
        val activeNotifications = notificationManager.activeNotifications
        return activeNotifications.any { it.id == NOTIFICATION_ID }
    }
}
