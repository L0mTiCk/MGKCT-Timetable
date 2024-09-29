package com.l0mtick.mgkcttimetable.presentation.settings.components.other

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.l0mtick.mgkcttimetable.R
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsEvent

@Composable
fun NotificationRowItem(
    isAllowed: Boolean,
    onEvent: (SettingsEvent) -> Unit
) {
    OtherSectionRowBase(
        onRowClick = {  },
        title = stringResource(id = R.string.settings_notification_title),
        subtitle = stringResource(id = R.string.settings_notification_main)
    ) {
        Switch(
            modifier = Modifier.scale(.8f),
            checked = isAllowed,
            onCheckedChange = {
                onEvent(SettingsEvent.OnNotificationClick)
            }
        )
    }
}