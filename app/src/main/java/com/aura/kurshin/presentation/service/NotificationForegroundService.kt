package com.aura.kurshin.presentation.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.aura.kurshin.presentation.receiver.NotificationUtils

class NotificationForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        NotificationUtils.createNotificationChannel(this)

        if (NotificationUtils.isNotificationScheduled(this)) {
            NotificationUtils.scheduleNotification(this)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
