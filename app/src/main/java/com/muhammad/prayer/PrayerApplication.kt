package com.muhammad.prayer

import android.app.Application
import com.muhammad.prayer.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PrayerApplication : Application(){
    val applicationScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    companion object{
        lateinit var INSTANCE : PrayerApplication
    }
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin {
            androidContext(this@PrayerApplication)
            modules(appModule)
        }
    }
}