package com.muhammad.prayer.data.local

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import androidx.core.content.edit
import com.muhammad.prayer.PrayerApplication
import com.muhammad.prayer.domain.model.LocationData
import com.muhammad.prayer.domain.repository.LocationSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationDataStore() : LocationSettings {
    private val context = PrayerApplication.INSTANCE
    private var locationPrefs: SharedPreferences = context.applicationContext.getSharedPreferences(
        "location_prefs", Context.MODE_PRIVATE
    )

    override suspend fun saveLocation(location: LocationData) {
        locationPrefs.edit {
            putString("latitude", location.location?.latitude.toString())
            putString("longitude", location.location?.longitude.toString())
            putString("city", location.city)
        }
    }

    override fun getLocationFlow(): Flow<LocationData?> {
        return flow {
            val lat = locationPrefs.getString("latitude", null)
            val long= locationPrefs.getString("longitude", null)
            val city= locationPrefs.getString("city", null)
            if (lat != null && long != null && city != null) {
               emit(
                   LocationData(location = Location("saved").apply {
                       latitude = lat.toDouble()
                       longitude = long.toDouble()
                   }, city = city)
               )
            } else emit(null)
        }
    }
}