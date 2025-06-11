package com.muhammad.prayer.data.worker

import android.annotation.SuppressLint
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.muhammad.prayer.PrayerApplication
import com.muhammad.prayer.domain.model.Prayer
import java.util.concurrent.TimeUnit

@SuppressLint("RestrictedApi")
fun schedulePrayerNotifications(times: List<Prayer>, notificationEnableMap : Map<String,  Boolean>) {
    val context = PrayerApplication.INSTANCE
    times.forEach { (prayer, time) ->
        val isEnabled = notificationEnableMap[prayer] ?: false
        if(isEnabled){
            val delay = time.time - System.currentTimeMillis()
            if (delay > 0) {
                val data = Data.Builder().putString("title", "$prayer Prayer")
                    .putString("message", "It's time for $prayer Prayer").build()
                val request = OneTimeWorkRequestBuilder<PrayerNotificationWorker>().setInitialDelay(
                    delay,
                    TimeUnit.MILLISECONDS
                ).setInputData(data).addTag("").build()
                WorkManager.getInstance(context = context).enqueueUniqueWork(
                    "${prayer}_name", ExistingWorkPolicy.REPLACE, request
                )
            }
        }
    }
}