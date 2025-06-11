package com.muhammad.prayer.presentation.screen.prayer

import com.muhammad.prayer.domain.model.Prayer

sealed interface PrayerAction{
    data object OnLocationTrack : PrayerAction
    data object OnToggleLocationPermissionDialog : PrayerAction
    data object OnToggleLocationEnableDialog : PrayerAction
    data object OnLocationPermissionGranted : PrayerAction
    data object OnNotificationPermanentDenied : PrayerAction
    data object OnNotificationPermissionGranted : PrayerAction
    data class IsPrayerNotificationEnable(val prayer : String) : PrayerAction
    data class OnNotificationToggle(val prayer : String) : PrayerAction
    data object OnToggleNotificationPermissionDialog : PrayerAction
}