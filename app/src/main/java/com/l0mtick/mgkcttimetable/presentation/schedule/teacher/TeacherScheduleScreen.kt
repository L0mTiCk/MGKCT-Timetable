package com.l0mtick.mgkcttimetable.presentation.schedule.teacher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import com.l0mtick.mgkcttimetable.presentation.components.NoConnectionCard
import com.l0mtick.mgkcttimetable.presentation.components.NoLessonsCard
import com.l0mtick.mgkcttimetable.presentation.components.ScheduleDayCard
import com.l0mtick.mgkcttimetable.presentation.components.UpdateStatusBar
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TeacherScheduleScreen(
    scheduleRepository: ScheduleRepository,
    navController: NavController
) {
    val scheduleScreenViewModelFactory =
        TeacherScheduleScreenViewModelFactory(scheduleRepository = scheduleRepository)
    val teacherScheduleScreenViewModel: TeacherScheduleScreenViewModel =
        viewModel(factory = scheduleScreenViewModelFactory)
    val state = teacherScheduleScreenViewModel.state.collectAsState().value
    val onEvent = teacherScheduleScreenViewModel::onEvent
    val context = LocalContext.current
    val weekSchedule = state.groupSchedule
    DisposableEffect(Unit) {
        scheduleRepository.getConnectionStatus(
            context = context,
            callback = {
                onEvent(ScheduleEvent.OnNetworkChange(it))
            }
        )
        onDispose {
            scheduleRepository.unsubscribeConnectionStatus()
        }
    }
    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = state.isScheduleUpdating,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        strokeWidth = 5.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
            AnimatedVisibility(
                visible = !state.isScheduleUpdating,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = it
                ) {
                    stickyHeader {
                        TopAppBar(
                            title = {
                                Text(
                                    text = state.selectedGroup
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
                    if (weekSchedule.days != null) {
                        items(state.groupSchedule.days ?: emptyList()) {
                            ScheduleDayCard(
                                state = state,
                                onEvent = onEvent,
                                daySchedule = it
                            )
                        }
                    }
                    if (weekSchedule.days?.all {
                            it.lessons.isNullOrEmpty()
                        } != false) {
                        item {
                            NoLessonsCard()
                        }
                    }
                    item {
                        UpdateStatusBar(state = state)
                    }
                }
            }
        }
        NoConnectionCard(isVisible = !state.isConnected)
    }
}
