package com.muhammad.prayer.presentation.screen.prayer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AppAlertDialog(
    onDismiss: () -> Unit,
    title: String? = null, message: String? = null,
    confirmText: String? = null, titleContent: @Composable () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    cancelText: String? = null, optionText: String? = null, onOptionClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .width(260.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                } else {
                    titleContent()
                }
                if (message != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Spacer(Modifier.height(24.dp))
                if (confirmText != null) {
                    AppDivider()
                    TextButton(
                        onClick = onConfirmClick,
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 45.dp)
                    ) {
                        Text(
                            text = confirmText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                if (cancelText != null) {
                    AppDivider()
                    TextButton(
                        onClick = onCancelClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 45.dp),
                        shape = RoundedCornerShape(0.dp),
                    ) {
                        Text(
                            text = cancelText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
                if (optionText != null) {
                    AppDivider()
                    TextButton(
                        onClick = onOptionClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 45.dp),
                        shape = RoundedCornerShape(0.dp),
                    ) {
                        Text(
                            text = optionText, color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}