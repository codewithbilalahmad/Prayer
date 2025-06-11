package com.muhammad.prayer.presentation.screen.prayer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.muhammad.prayer.R
import com.muhammad.prayer.domain.model.Prayer
import com.muhammad.prayer.presentation.screen.prayer.components.AppAlertDialog
import com.muhammad.prayer.presentation.screen.prayer.components.AppDivider
import com.muhammad.prayer.presentation.screen.prayer.components.PrayerItem
import com.muhammad.prayer.presentation.screen.prayer.components.PrayerNotificationItem
import com.muhammad.prayer.presentation.screen.prayer.components.PrayerTopBar
import com.muhammad.prayer.utils.ObserveAsEvent
import com.muhammad.prayer.utils.navigateToLocationSettings
import com.muhammad.prayer.utils.navigateToSettings
import kotlinx.coroutines.flow.Flow

@SuppressLint("InlinedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(
    state: State<PrayerState>,
    onAction: (PrayerAction) -> Unit,
    events: Flow<PrayerEvent>,
    prayerTimes: List<Prayer>,
    notificationEnableMap: Map<String, Boolean>,
) {
    val context = LocalContext.current
    val activity = (context as Activity)
    val message = state.value.message
    val city = state.value.city
    val showLocationPermissionAccessDialog = state.value.showLocationPermissionAccessDialog
    val showNotificationPermissionAccessDialog = state.value.showNotificationPermissionAccessDialog
    val notificationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
        if(isGranted){
            onAction(PrayerAction.OnNotificationPermissionGranted)
        } else{
            onAction(PrayerAction.OnNotificationPermanentDenied)
        }
    }
    val locationPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            when {
                fineLocationGranted || coarseLocationGranted -> {
                    onAction(PrayerAction.OnLocationPermissionGranted)
                }

                !fineLocationGranted && !coarseLocationGranted && !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) && !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    onAction(PrayerAction.OnToggleLocationPermissionDialog)
                }
            }
        }
    ObserveAsEvent(events) { event ->
        when (event) {
            PrayerEvent.OnRequestLocationPermission -> {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        PrayerTopBar(onLocationTrack = {
            onAction(PrayerAction.OnLocationTrack)
        })
        Spacer(Modifier.height(30.dp))
        AnimatedContent(prayerTimes.isNotEmpty()) { showPrayerTimes ->
            if (showPrayerTimes) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Prayer times in ${city.orEmpty()}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        maxItemsInEachRow = 3,
                        itemVerticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        prayerTimes.forEach { prayer ->
                            PrayerItem(
                                modifier = Modifier.wrapContentSize(),
                                prayerItem = prayer
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    AppDivider()
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = "Get Prayer Notifications",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        )
                        Spacer(Modifier.width(2.dp))
                        if(!state.value.isNotificationPermissionGranted){
                            IconButton(onClick = {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.notification_off),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    prayerTimes.forEach { prayer ->
                        val name = prayer.name
                        val isEnable = notificationEnableMap[name] == true
                        PrayerNotificationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            prayer = prayer,
                            isEnable = isEnable,
                            onNotificationClick = {
                                onAction(PrayerAction.OnNotificationToggle(name))
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.sujud),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    if (message != null) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Button(onClick = {
                        onAction(PrayerAction.OnLocationTrack)
                    }) {
                        Text(text = "Track Location")
                    }
                }
            }
        }
    }
    if (showLocationPermissionAccessDialog) {
        AppAlertDialog(
            onDismiss = {
                onAction(PrayerAction.OnToggleLocationPermissionDialog)
            },
            title = "Allow Location",
            message = "Allow your location to Prayer to show you prayer times!",
            confirmText = "Confirm", cancelText = "Denied", onConfirmClick = {
                onAction(PrayerAction.OnToggleLocationPermissionDialog)
                navigateToSettings()
            }, onCancelClick = {
                onAction(PrayerAction.OnToggleLocationPermissionDialog)
            }
        )
    }
    if(showNotificationPermissionAccessDialog){
        AppAlertDialog(
            onDismiss = {
                onAction(PrayerAction.OnToggleNotificationPermissionDialog)
            },
            title = "Allow Notifications",
            message = "Allow notifications to Prayer to send you notification when prayer time!",
            confirmText = "Confirm", cancelText = "Denied", onConfirmClick = {
                onAction(PrayerAction.OnToggleNotificationPermissionDialog)
                navigateToSettings()
            }, onCancelClick = {
                onAction(PrayerAction.OnToggleNotificationPermissionDialog)
            }
        )
    }
    if (state.value.showLocationEnableDialog) {
        AppAlertDialog(
            onDismiss = {
                onAction(PrayerAction.OnToggleLocationEnableDialog)
            },
            title = "Enable Location",
            message = "Location is disable in your device!",
            confirmText = "Confirm", cancelText = "Denied", onConfirmClick = {
                onAction(PrayerAction.OnToggleLocationEnableDialog)
                navigateToLocationSettings()
            }, onCancelClick = {
                onAction(PrayerAction.OnToggleLocationEnableDialog)
            }
        )
    }
    if (state.value.isGettingLocation) {
        AppAlertDialog(onDismiss = {

        }, title = "Getting Location", message = "Please wait while we get your location")
    }
}