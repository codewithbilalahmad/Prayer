package com.muhammad.prayer.presentation.screen.prayer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.prayer.R
import com.muhammad.prayer.presentation.theme.AppTheme

@Composable
fun PrayerTopBar(modifier: Modifier = Modifier,onLocationTrack: () -> Unit) {
    Box(modifier = modifier.height(IntrinsicSize.Max)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = AppTheme.colors.secondarySurface,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        )
        Content(modifier = Modifier.statusBarsPadding(), onLocationTrack = onLocationTrack)
    }
}

@Composable
private fun Content(modifier: Modifier = Modifier,onLocationTrack: () -> Unit) {
    Column(modifier = modifier.fillMaxWidth()) {
        ActionBar(
            headline = "Prayer",
            modifier = modifier
                .padding(horizontal = 19.dp, vertical = 6.dp), onLocationTrack = onLocationTrack
        )
        Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Image(
                painter = painterResource(R.drawable.islam),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(256.dp).align(Alignment.BottomEnd).offset(y = 20.dp, x = -(20).dp)
            )
            ProductHighlights(modifier = Modifier.padding(start = 16.dp))
        }
    }
}

@Composable
private fun ActionBar(modifier: Modifier = Modifier, headline: String,onLocationTrack: () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = headline,
            style = MaterialTheme.typography.headlineLarge,
            color = AppTheme.colors.onSecondarySurface
        )
        CloseButton(onLocationTrack = onLocationTrack)
    }
}

@Composable
fun CloseButton(modifier: Modifier = Modifier, onLocationTrack : () -> Unit) {
    Surface(
        modifier = modifier.size(44.dp),
        shape = RoundedCornerShape(16.dp), onClick = onLocationTrack,
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.location),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ProductHighlights(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Highlight(
            text = "Close to Prayer",
            color = AppTheme.colors.actionSurface,
            contentColor = AppTheme.colors.onActionSurface
        )
        Highlight(
            text = "Close to Allah",
            color = AppTheme.colors.highlightSurface,
            contentColor = AppTheme.colors.onHighlightSurface
        )
    }
}

@Composable
private fun Highlight(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    contentColor: Color,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = color,
        contentColor = contentColor
    ) {
        Box(modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}