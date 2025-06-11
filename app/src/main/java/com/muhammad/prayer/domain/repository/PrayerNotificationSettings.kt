package com.muhammad.prayer.domain.repository

import kotlinx.coroutines.flow.Flow

interface PrayerNotificationSettings {
    suspend fun setNotificationEnable(name : String, enable : Boolean)
    fun isNotificationEnable(name : String) : Flow<Boolean>
}