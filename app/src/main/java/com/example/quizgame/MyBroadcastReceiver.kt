package com.example.quizgame

import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = getActivity(context,0, intent,0)



        val builder : NotificationCompat.Builder? = context?.let { NotificationCompat.Builder(it, "quizChannel")
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle("QuizGames")
            .setContentText("We miss you!")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }
        with(context?.let { NotificationManagerCompat.from(it) }) {
            if (builder != null) {
                this?.notify(101, builder.build())
            }
        }
    }


}