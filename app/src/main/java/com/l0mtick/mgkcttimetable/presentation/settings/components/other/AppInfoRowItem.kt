package com.l0mtick.mgkcttimetable.presentation.settings.components.other

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.l0mtick.mgkcttimetable.R
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsEvent

@Composable
fun AppInfoRowItem(onEvent: (SettingsEvent) -> Unit) {
    OtherSectionRowBase(
        onRowClick = {
            onEvent(SettingsEvent.OnAppInfoClick)
            Firebase.analytics.logEvent("about_app_pressed") {}
        },
        title = stringResource(id = R.string.settings_about_title),
        subtitle = stringResource(
            id = R.string.settings_about_main
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "Info icon",
            modifier = Modifier.size(25.dp)
        )
    }
}