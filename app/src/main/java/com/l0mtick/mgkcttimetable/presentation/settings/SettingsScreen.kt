package com.l0mtick.mgkcttimetable.presentation.settings

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.l0mtick.mgkcttimetable.R
import com.l0mtick.mgkcttimetable.presentation.settings.components.AppInfoDialogBox
import com.l0mtick.mgkcttimetable.presentation.settings.components.other.AppInfoRowItem
import com.l0mtick.mgkcttimetable.presentation.settings.components.other.NotificationRowItem
import com.l0mtick.mgkcttimetable.presentation.settings.components.OutlinedSelector
import com.l0mtick.mgkcttimetable.presentation.settings.components.other.WidgetSettingRowItem
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsScreenViewModel: SettingsScreenViewModel
) {
    val state = settingsScreenViewModel.state.collectAsState().value
    val onEvent = settingsScreenViewModel::onEvent
    val onGroupEvent: (String) -> Unit = { groupName ->
        settingsScreenViewModel.onEvent(SettingsEvent.OnSpecificGroupClick(groupName))
    }

    val onTeacherEvent: (String) -> Unit = { teacherName ->
        settingsScreenViewModel.onEvent(SettingsEvent.OnSpecificTeacherClick(teacherName))
    }

    AnimatedVisibility(
        visible = state.isAppInfoDialogOpen,
        enter = fadeIn(animationSpec = tween(100)),
        exit = fadeOut(animationSpec = tween(100))
    ) {
        AppInfoDialogBox(onEvent = onEvent)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_header)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            try {
                                val previousRoute =
                                    navController.previousBackStackEntry?.destination?.route
                                navController.navigate(
                                    route = previousRoute ?: throw IllegalArgumentException()
                                ) {
                                    popUpTo(route = previousRoute) {
                                        inclusive = true
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("timetableTest", "Error while navigating from settings")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.ArrowBack,
                            contentDescription = "Back arrow"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = it.calculateTopPadding())
                .fillMaxSize()
        ) {
            item {
                OutlinedSelector(
                    label = stringResource(id = R.string.navigation_group),
                    value = state.selectedGroup,
                    elements = state.allGroups,
                    onEvent = onGroupEvent
                )
                OutlinedSelector(
                    label = stringResource(id = R.string.navigation_teacher),
                    value = state.selectedTeacher,
                    elements = state.allTeachers,
                    onEvent = onTeacherEvent
                )
                HorizontalDivider(
                    modifier = Modifier.padding(top = 30.dp)
                )
                Column(
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.settings_other),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
//                Spacer(modifier = Modifier.height(10.dp))
//                ContactDeveloperRowItem()
                    Spacer(modifier = Modifier.height(20.dp))
                    NotificationRowItem(isAllowed = state.isNotificationsEnabled, onEvent = onEvent)
                    Spacer(modifier = Modifier.height(10.dp))
                    AppInfoRowItem(onEvent = onEvent)
                    Spacer(modifier = Modifier.height(10.dp))
                    WidgetSettingRowItem(state.isWidgetGroupSelected, onEvent = onEvent)
                }
            }
        }
    }
}