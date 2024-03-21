package com.l0mtick.mgkcttimetable.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l0mtick.mgkcttimetable.domain.model.schedule.DaySchedule
import com.l0mtick.mgkcttimetable.domain.model.schedule.Lesson
import com.l0mtick.mgkcttimetable.domain.model.schedule.ScheduleUnion
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleEvent
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDayCard(
    state: ScheduleState,
    daySchedule: DaySchedule,
    onEvent: (ScheduleEvent) -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (state.selectedDay == daySchedule.date) 180f else 0f,
        label = "Arrow rotation"
    )
    if (!daySchedule.lessons.isNullOrEmpty()) {
        Card(
            onClick = {
                Log.w("timetableTest", "Card click!")
                onEvent(ScheduleEvent.OnSpecificDayClick(daySchedule.date))
            },
            elevation = CardDefaults.elevatedCardElevation(),
            modifier = Modifier
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${daySchedule.date.substringBeforeLast('.')}, ${daySchedule.weekday}",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                )
                IconButton(
                    onClick = {
                        onEvent(ScheduleEvent.OnSpecificDayClick(daySchedule.date))
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Arrow",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .rotate(rotationAngle)
                    )
                }
            }
            AnimatedVisibility(
                visible = state.selectedDay == daySchedule.date,
            ) {
                Column {
                    daySchedule.lessons.forEach { scheduleUnion ->
                        if (scheduleUnion != null) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        //TODO: return color highlighting for current lesson
                                        color = if (daySchedule.date == LocalDateTime.now().format(
                                                DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                            && getLessonNumber(scheduleUnion) == state.currentLesson)
                                            MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                when (scheduleUnion) {
                                    is ScheduleUnion.LessonArrayValue -> {
                                        Text(
                                            text = scheduleUnion.value[0].number.toString(),
                                            modifier = Modifier
                                                .padding(end = 15.dp),
                                            fontSize = 18.sp
                                        )
                                        Column {
                                            scheduleUnion.value.forEach { lesson ->
                                                LessonRow(lesson = lesson)
                                            }
                                        }
                                    }

                                    is ScheduleUnion.LessonValue -> {
                                        Text(
                                            text = scheduleUnion.value.number.toString(),
                                            modifier = Modifier
                                                .padding(end = 15.dp),
                                            fontSize = 18.sp
                                        )
                                        LessonRow(lesson = scheduleUnion.value)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonRow(lesson: Lesson) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        val subgroupString = if (lesson.subgroup != null) {
            "${lesson.subgroup}. "
        } else {
            ""
        }
        val teacherString = if (lesson.teacher != null) {
            "\n${lesson.teacher}"
        } else {
            ""
        }
        val commentString = if (teacherString == "" && !lesson.comment.isNullOrEmpty())
            "\n${(lesson.comment)}"
        else
            "  ${(lesson.comment ?: "")}"
        val mainString = subgroupString +
                lesson.name.toString() +
                " (${lesson.type ?: ""})" +
                teacherString +
                commentString
        Text(
            text = mainString,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = lesson.cabinet ?: "-",
        )
    }
}

private fun getLessonNumber(scheduleUnion: ScheduleUnion): Int {
    return when (scheduleUnion) {
        is ScheduleUnion.LessonArrayValue -> scheduleUnion.value[0].number ?: 0;
        is ScheduleUnion.LessonValue -> scheduleUnion.value.number ?: 0;
        else -> 0;
    }
}