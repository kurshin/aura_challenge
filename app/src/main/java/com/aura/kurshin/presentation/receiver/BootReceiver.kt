package com.aura.kurshin.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import java.text.SimpleDateFormat
import java.util.*

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val sharedPreferences = context.getSharedPreferences("BootEvents", Context.MODE_PRIVATE)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val bootTime = dateFormat.format(Date())

            // Save boot event
            sharedPreferences.edit {
                putString("last_boot_time", bootTime)
                val bootCount = sharedPreferences.getInt("boot_count", 0) + 1
                putInt("boot_count", bootCount)

                // Store last boot time with index
                putString("boot_time_$bootCount", bootTime)
                putInt("boot_count_$bootCount", 1)
            }

            // Reschedule notification if it was present before reboot
            if (NotificationUtils.isNotificationScheduled(context)) {
                NotificationUtils.scheduleNotification(context)
            }
        }
    }
}
