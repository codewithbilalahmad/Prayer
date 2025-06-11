@file:Suppress("DEPRECATION")

package com.muhammad.prayer.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.HighLatitudeRule
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.google.android.gms.location.LocationServices
import com.muhammad.prayer.PrayerApplication
import com.muhammad.prayer.domain.model.LocationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun getLastKnownLocation(): LocationData? {
    val context = PrayerApplication.INSTANCE
    val locationClient = LocationServices.getFusedLocationProviderClient(context)
    return suspendCoroutine { cont ->
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@suspendCoroutine
        }
        locationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val geocoder = Geocoder(context)
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val city = addresses?.get(0)?.locality ?: ""
                    val locationData = LocationData(location, city)
                    cont.resumeWith(Result.success(locationData))
                }
            }
            .addOnFailureListener { e ->
                cont.resume(null)
            }
    }
}

@SuppressLint("NewApi")
fun getPrayerTimes(location: Location, date : LocalDate = LocalDate.now()): Map<String, Date> {
    val coordinates = Coordinates(location.latitude, location.longitude)

    val params = CalculationMethod.UMM_AL_QURA.parameters.apply {
        madhab = Madhab.SHAFI
        highLatitudeRule = HighLatitudeRule.TWILIGHT_ANGLE
    }

    val dateComponents = DateComponents(date.year, date.monthValue, date.dayOfMonth)
    val prayerTimes = PrayerTimes(coordinates, dateComponents, params)

    return mapOf(
        "Fajr" to Date.from(prayerTimes.fajr.toInstant()),
        "Dhuhr" to Date.from(prayerTimes.dhuhr.toInstant()),
        "Asr" to Date.from(prayerTimes.asr.toInstant()),
        "Maghrib" to Date.from(prayerTimes.maghrib.toInstant()),
        "Isha" to Date.from(prayerTimes.isha.toInstant())
    )

}

@Composable
fun <T> ObserveAsEvent(flow: Flow<T>, key1: Any? = null, key2: Any? = null, onEvent: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}

fun isLocationEnable(): Boolean {
    val context = PrayerApplication.INSTANCE
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}
fun navigateToLocationSettings(){
    val context = PrayerApplication.INSTANCE
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
        flags = FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}
fun navigateToSettings(){
    val context = PrayerApplication.INSTANCE
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        flags = FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}