package com.l0mtick.mgkcttimetable.presentation.settings.components.other

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l0mtick.mgkcttimetable.R
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsEvent

@Composable
fun WidgetSettingRowItem(isWidgetGroupSelected: Boolean, onEvent: (SettingsEvent) -> Unit) {
    OtherSectionRowBase(
        onRowClick = { },
        title = stringResource(id = R.string.settings_widget_title),
        subtitle = stringResource(id = R.string.settings_widget_main)
    ) {
        var isExpanded by remember {
            mutableStateOf(false)
        }
        AssistChip(
            onClick = { isExpanded = true },
            label = {
                Text(
                    text = stringResource(if (isWidgetGroupSelected) R.string.navigation_group else R.string.navigation_teacher),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "Select widget icon"
                )
            },
            modifier = Modifier
                .width(120.dp)
        )
        Box(contentAlignment = Alignment.TopEnd) {
            DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.navigation_group)) },
                    onClick = {
                        onEvent(SettingsEvent.OnWidgetSelectionChanged(true))
                        isExpanded = false
                    })
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.navigation_teacher)) },
                    onClick = {
                        onEvent(SettingsEvent.OnWidgetSelectionChanged(false))
                        isExpanded = false
                    })
            }
        }
    }
}