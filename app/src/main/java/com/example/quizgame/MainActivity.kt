package com.example.quizgame

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = "Notification title"
        val descText = "Notification text"
        val importance : Int = NotificationManager.IMPORTANCE_DEFAULT
        val channel : NotificationChannel = NotificationChannel("quizChannel", name, importance).apply {
            description = descText
        }
        val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this.applicationContext, 234, intent, 0
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    override fun onStop() {
        super.onStop()
        val timeInSec = 15
        val intent = Intent(this, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this.applicationContext, 234, intent, 0
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeInSec * 1000] = pendingIntent
    }
}