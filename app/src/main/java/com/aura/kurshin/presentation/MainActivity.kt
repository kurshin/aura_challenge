package com.aura.kurshin.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aura.kurshin.R
import com.aura.kurshin.presentation.receiver.NotificationUtils
import com.aura.kurshin.presentation.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }

        NotificationUtils.createNotificationChannel(this)
    }
}