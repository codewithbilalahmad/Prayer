package com.muhammad.prayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammad.prayer.data.worker.schedulePrayerNotifications
import com.muhammad.prayer.presentation.screen.prayer.PrayerScreen
import com.muhammad.prayer.presentation.theme.PrayerTheme
import com.muhammad.prayer.presentation.viewmodel.PrayerViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrayerTheme {
                val viewModel = koinViewModel<PrayerViewModel>()
                val state = viewModel.state.collectAsStateWithLifecycle()
                val prayerTimes = state.value.prayerTimes
                val notificationEnableMap = state.value.notificationEnableMap
                PrayerScreen(
                    state = state,
                    onAction = viewModel::onAction,notificationEnableMap = notificationEnableMap,
                    events = viewModel.events,prayerTimes = prayerTimes
                )
                LaunchedEffect(prayerTimes) {
                    schedulePrayerNotifications(prayerTimes, notificationEnableMap)
                }
            }
        }
    }
}