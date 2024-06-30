package com.l0mtick.mgkcttimetable.presentation.schedule.group

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.navigation.NavController
import com.l0mtick.mgkcttimetable.presentation.components.NoConnectionCard
import com.l0mtick.mgkcttimetable.presentation.components.NoLessonsCard
import com.l0mtick.mgkcttimetable.presentation.components.ScheduleDayCard
import com.l0mtick.mgkcttimetable.presentation.components.ScheduleTopBar
import com.l0mtick.mgkcttimetable.presentation.components.UpdateStatusBar
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GroupScheduleScreen(
    navController: NavController,
    groupScheduleScreenViewModel: GroupScheduleScreenViewModel
) {
    val state = groupScheduleScreenViewModel.state.collectAsState().value
    val onEvent = groupScheduleScreenViewModel::onEvent
    val pullToRefreshState = rememberPullToRefreshState()
    val lazyListState: LazyListState = rememberLazyListState()
    val context = LocalContext.current
    val vibrator = remember {
        context.getSystemService(Vibrator::class.java)
    }
    val haptic = LocalHapticFeedback.current

    val weekSchedule = state.groupSchedule
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            AnimatedVisibility(
                visible = !state.isScheduleUpdating,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = it
                ) {
                    stickyHeader {
                        ScheduleTopBar(state = state, onEvent = onEvent, navController = navController)
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

            if (pullToRefreshState.isRefreshing) {
                LaunchedEffect(key1 = true) {
                    if (!state.isScheduleUpdating && state.isConnected) {
                        onEvent(ScheduleEvent.UpdateSchedule)
                    } else if (!state.isConnected) {
                        pullToRefreshState.endRefresh()
                    }
                }
            }

            LaunchedEffect(key1 = state.isScheduleUpdating) {
                if (state.isScheduleUpdating) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                    } else {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                    pullToRefreshState.startRefresh()
                } else {
                    pullToRefreshState.endRefresh()
                }
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }
    }
    NoConnectionCard(isVisible = !state.isConnected)
}

