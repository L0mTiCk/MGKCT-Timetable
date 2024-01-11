package com.l0mtick.mgkcttimetable.presentation.schedule.group

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
import com.l0mtick.mgkcttimetable.presentation.components.ScheduleDayCard
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GroupScheduleScreen(
    scheduleRepository: ScheduleRepository,
    navController: NavController
) {
    val groupScheduleScreenViewModelFactory =
        GroupScheduleScreenViewModelFactory(scheduleRepository = scheduleRepository)
    val groupScheduleScreenViewModel: GroupScheduleScreenViewModel =
        viewModel(factory = groupScheduleScreenViewModelFactory)
    val state = groupScheduleScreenViewModel.state.collectAsState().value
    val onEvent = groupScheduleScreenViewModel::onEvent
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
            //contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = state.isScheduleUpdating,
                enter = fadeIn(animationSpec = tween(150)),
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
                                    text = "Группа - ${state.selectedGroup}"
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
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Пар нет",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    NoConnectionCard(isVisible = !state.isConnected)
}

