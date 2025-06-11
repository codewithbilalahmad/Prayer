package com.muhammad.prayer.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun PrayerTheme(content : @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppColors provides extendedColor
    ) {
        MaterialTheme(content = content)
    }
}
object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}