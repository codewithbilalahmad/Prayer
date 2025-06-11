package com.muhammad.prayer.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.muhammad.prayer.PrayerApplication
import com.muhammad.prayer.domain.repository.PrayerNotificationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PrayerNotificationDataStore : PrayerNotificationSettings {
    private val context = PrayerApplication.INSTANCE
    private var notificationPrefs: SharedPreferences = context.applicationContext.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
    private val listeners = mutableMapOf<String, MutableStateFlow<Boolean>>()
    override suspend fun setNotificationEnable(name: String, enable: Boolean) {
        notificationPrefs.edit{
            putBoolean(name, enable)
        }
        listeners[name]?.value = enable
    }

    override fun isNotificationEnable(name: String): Flow<Boolean> {
        return listeners.getOrPut(name){
            val stateFlow = MutableStateFlow(notificationPrefs.getBoolean(name, false))
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == name) {
                    stateFlow.value = notificationPrefs.getBoolean(name, false)
                }
            }
            notificationPrefs.registerOnSharedPreferenceChangeListener(listener)
            stateFlow
        }
    }
}