package com.aura.kurshin.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.aura.kurshin.presentation.service.NotificationForegroundService
import java.text.SimpleDateFormat
import java.util.*

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("1111", "received = ${intent.action}")
        if (intent.action == "com.example.myapp.CUSTOM_ACTION") {

//        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
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

            val serviceIntent = Intent(context, NotificationForegroundService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}
