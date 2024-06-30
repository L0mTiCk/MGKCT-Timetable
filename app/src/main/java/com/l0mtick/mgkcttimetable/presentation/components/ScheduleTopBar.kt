package com.l0mtick.mgkcttimetable.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.l0mtick.mgkcttimetable.R
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleEvent
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleTopBar(
    modifier: Modifier = Modifier,
    state: ScheduleState,
    onEvent: (ScheduleEvent) -> Unit,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(
                text =
                if(state.selectedGroup.toDoubleOrNull() != null)
                    "${stringResource(id = R.string.screen_group_title)} - ${state.selectedGroup}"
                else
                    state.selectedGroup
            )
        },
        actions = {
            IconButton(onClick = {
                if (!state.isScheduleUpdating && state.isConnected)
                    onEvent(ScheduleEvent.UpdateSchedule)
            }) {
                Icon(
                    imageVector = Icons.TwoTone.Refresh,
                    contentDescription = "Refresh",
                )
            }
            IconButton(onClick = {
                navController.navigate("settings")
            }) {
                Icon(
                    imageVector = Icons.TwoTone.Settings,
                    contentDescription = "Settings",
                )
            }
        }
    )
}