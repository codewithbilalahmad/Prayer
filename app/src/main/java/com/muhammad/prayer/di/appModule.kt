package com.muhammad.prayer.di

import com.muhammad.prayer.data.local.LocationDataStore
import com.muhammad.prayer.data.local.PrayerNotificationDataStore
import com.muhammad.prayer.presentation.viewmodel.PrayerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { LocationDataStore() }
    single { PrayerNotificationDataStore() }
    viewModelOf(::PrayerViewModel)
}