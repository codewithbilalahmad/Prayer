package com.muhammad.prayer.presentation.screen.prayer

import com.muhammad.prayer.domain.model.Prayer

data class PrayerState(
    val prayerTimes: List<Prayer> = emptyList(),
    val message: String? = null,
    val city : String?=null,
    val isNotificationPermissionGranted : Boolean = false,
    val notificationEnableMap : Map<String, Boolean> = emptyMap(),
    val isGettingLocation : Boolean = false,
    val showLocationPermissionAccessDialog: Boolean = false,
    val showNotificationPermissionAccessDialog: Boolean = false,
    val showLocationEnableDialog: Boolean = false
)
