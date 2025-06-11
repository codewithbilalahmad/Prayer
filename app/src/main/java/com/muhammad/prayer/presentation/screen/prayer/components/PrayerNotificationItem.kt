package com.muhammad.prayer.presentation.screen.prayer.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.prayer.R
import com.muhammad.prayer.domain.model.Prayer
import com.muhammad.prayer.presentation.theme.AppTheme
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@SuppressLint("SimpleDateFormat")
@Composable
fun PrayerNotificationItem(
    modifier: Modifier,
    prayer: Prayer,
    isEnable: Boolean,
    onNotificationClick: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 0.5.dp,
            AppTheme.colors.secondarySurface
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = AppTheme.colors.background,
            contentColor = AppTheme.colors.onBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.sujud),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
            Text(
                text = prayer.name, style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = SimpleDateFormat("hh:mm a").format(prayer.date),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            FilledIconButton(
                onClick = {
                    onNotificationClick()
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = AppTheme.colors.secondarySurface,
                    contentColor = AppTheme.colors.onSecondarySurface
                ), shapes = IconButtonDefaults.shapes()
            ) {
                val icon = if (isEnable) R.drawable.notification_on else R.drawable.notification_off
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}