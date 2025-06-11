package com.muhammad.prayer.data.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.muhammad.prayer.MainActivity
import com.muhammad.prayer.R

class PrayerNotificationWorker(
    context: Context,
    params: WorkerParameters,
) : Worker(context, params) {
    override fun doWork(): Result {
        val title = inputData.getString("title") ?: "Prayer Time"
        val message = inputData.getString("message") ?: "It's time to pray!"
        showNotification(applicationContext, title = title, message = message)
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, title: String, message:  String) {
        val channelId = "prayer_channel"
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Prayer Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message).setDeleteIntent(clickPendingIntent(context))
            .setSmallIcon(R.drawable.sujud).setSound(Settings.System.DEFAULT_RINGTONE_URI)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
    fun clickPendingIntent(context : Context) : PendingIntent{
        val intent = Intent(context, MainActivity::class.java)
        return TaskStackBuilder.create(context).run{
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
    }
}