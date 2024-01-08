package com.l0mtick.mgkcttimetable.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsEvent

@Composable
fun AppInfoRowItem(onEvent: (SettingsEvent) -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onEvent(SettingsEvent.OnAppInfoClick)
            }
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                Column {
                    Text(
                        text = "О приложении",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Получение информации о данном приложении.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f)
                    )
                }
            }
            Spacer(modifier = Modifier.width(15.dp))
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Mail icon",
                modifier = Modifier.size(25.dp)
            )
        }
    }
}