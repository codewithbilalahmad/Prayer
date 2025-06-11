package com.muhammad.prayer.presentation.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammad.prayer.PrayerApplication
import com.muhammad.prayer.data.local.LocationDataStore
import com.muhammad.prayer.data.local.PrayerNotificationDataStore
import com.muhammad.prayer.domain.model.Prayer
import com.muhammad.prayer.presentation.screen.prayer.PrayerAction
import com.muhammad.prayer.presentation.screen.prayer.PrayerEvent
import com.muhammad.prayer.presentation.screen.prayer.PrayerState
import com.muhammad.prayer.utils.getLastKnownLocation
import com.muhammad.prayer.utils.getPrayerTimes
import com.muhammad.prayer.utils.isLocationEnable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrayerViewModel(
    private val locationDataStore: LocationDataStore,
    private val notificationDataStore: PrayerNotificationDataStore,
) : ViewModel() {
    private val context = PrayerApplication.INSTANCE
    private val _state = MutableStateFlow(PrayerState())
    val state = _state.asStateFlow()
    private val _events = Channel<PrayerEvent>()
    val events = _events.receiveAsFlow()

    init {
        checkNotificationPermission()
        observeLocationSettings()
    }

    @SuppressLint("InlinedApi")
    fun checkNotificationPermission() {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        _state.update { it.copy(isNotificationPermissionGranted = isGranted) }
    }

    fun onAction(action: PrayerAction) {
        when (action) {
            is PrayerAction.OnLocationTrack -> {
                requestLocationPermission()
            }

            PrayerAction.OnLocationPermissionGranted -> {
                onLocationPermissionGranted()
            }

            PrayerAction.OnToggleLocationPermissionDialog -> {
                onToggleLocationPermissionDialog()
            }

            PrayerAction.OnToggleLocationEnableDialog -> onToggleLocationEnableDialog()
            is PrayerAction.IsPrayerNotificationEnable -> {
                viewModelScope.launch {
                    val isEnabled = isPrayerNotificationEnable(action.prayer)
                    _state.update { it.copy(notificationEnableMap = state.value.notificationEnableMap + (action.prayer to isEnabled)) }
                }
            }

            is PrayerAction.OnNotificationToggle -> {
                onNotificationToggle(action.prayer)
            }

            PrayerAction.OnNotificationPermanentDenied -> {
                onNotificationPermanentDenied()
            }

            PrayerAction.OnToggleNotificationPermissionDialog -> {
                _state.update {
                    it.copy(
                        showNotificationPermissionAccessDialog = !state.value.showNotificationPermissionAccessDialog,
                    )
                }
            }

            PrayerAction.OnNotificationPermissionGranted -> {
                _state.update {
                    it.copy(
                        isNotificationPermissionGranted = true
                    )
                }
            }
        }
    }

    private fun onNotificationPermanentDenied() {
        _state.update {
            it.copy(
                showNotificationPermissionAccessDialog = !state.value.showNotificationPermissionAccessDialog,
                isNotificationPermissionGranted = false
            )
        }
    }

    private fun onNotificationToggle(prayer: String) {
        viewModelScope.launch {
            val enable = state.value.notificationEnableMap[prayer] ?: false
            notificationDataStore.setNotificationEnable(name = prayer, enable = !enable)
            onAction(PrayerAction.IsPrayerNotificationEnable(prayer))
        }
    }


    private fun onToggleLocationPermissionDialog() {
        _state.update { it.copy(showLocationPermissionAccessDialog = !state.value.showLocationPermissionAccessDialog) }
    }

    private fun onToggleLocationEnableDialog() {
        _state.update { it.copy(showLocationEnableDialog = !state.value.showLocationEnableDialog) }
    }

    private fun onLocationPermissionGranted() {
        viewModelScope.launch {
            val isLocationEnable = isLocationEnable()
            if (isLocationEnable) {
                _state.update { it.copy(isGettingLocation = true) }
                val location = getLastKnownLocation()
                _state.update { it.copy(isGettingLocation = false) }
                if (location != null) {
                    val prayerTimes = getPrayerTimes(location.location!!)
                    locationDataStore.saveLocation(location)
                    val prayerList = prayerTimes.map { (name, time) ->
                        Prayer(name, time)
                    }
                    prayerList.forEach { prayer ->
                        onAction(PrayerAction.IsPrayerNotificationEnable(prayer.name))
                    }
                    _state.update {
                        it.copy(
                            prayerTimes = prayerList,
                            city = location.city,
                            message = null
                        )
                    }
                } else {
                    _state.update { it.copy(message = "Something went wrong! Please check your internet connection") }
                }
            } else {
                _state.update {
                    it.copy(showLocationEnableDialog = true)
                }
            }
        }
    }

    private fun observeLocationSettings() {
        viewModelScope.launch {
            locationDataStore.getLocationFlow().collect { location ->
                if (location != null) {
                    val prayerTimes = getPrayerTimes(location.location!!)
                    val prayerList = prayerTimes.map { (name, time) ->
                        Prayer(name, time)
                    }
                    prayerList.forEach { prayer ->
                        onAction(PrayerAction.IsPrayerNotificationEnable(prayer.name))
                    }
                    _state.update {
                        it.copy(prayerTimes = prayerList, city = location.city)
                    }
                } else {
                    _state.update {
                        it.copy(message = "Track your location, to get your prayer times!")
                    }
                }
            }
        }
    }

    private suspend fun isPrayerNotificationEnable(prayer: String): Boolean {
        return notificationDataStore.isNotificationEnable(prayer).first()
    }

    private fun requestLocationPermission() {
        viewModelScope.launch {
            _events.send(PrayerEvent.OnRequestLocationPermission)
        }
    }
}