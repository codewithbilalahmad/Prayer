package com.muhammad.prayer.presentation.screen.prayer

sealed interface PrayerEvent{
    data object OnRequestLocationPermission : PrayerEvent
}