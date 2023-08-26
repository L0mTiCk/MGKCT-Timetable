package com.l0mtick.mgkcttimetable.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.l0mtick.mgkcttimetable.presentation.components.ScheduleDayCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StudentScheduleScreen(
    state: ScheduleState,
    onEvent: (ScheduleEvent) -> Unit
) {
    Scaffold(
        bottomBar = {

        }
    ) {
        AnimatedVisibility(
            visible = !state.isScheduleUpdating,
            enter = slideInHorizontally { it },
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = it
            ) {
                    val groupLessons = state.groupSchedule.get(0)
                    val groupAuditory = state.groupSchedule.get(1)
                    val groupLessonNumbers = state.groupSchedule.get(2)

                    stickyHeader {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "${state.selectedGroup} --- ${state.currentHour}"
                                )
                            },
                            actions = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.TwoTone.Settings,
                                        contentDescription = "Settings"
                                    )
                                }
                            }
                        )
                    }

                    items(groupLessons.keys.toList()) { key ->
                        ScheduleDayCard(
                            day = key,
                            lessons = groupLessons.get(key)!!,
                            auditory = groupAuditory.get(key)!!,
                            lessonNumbers = groupLessonNumbers.get(key)!!,
                            state = state,
                            onEvent = onEvent
                        )
                    }
                }
            }
    }
}