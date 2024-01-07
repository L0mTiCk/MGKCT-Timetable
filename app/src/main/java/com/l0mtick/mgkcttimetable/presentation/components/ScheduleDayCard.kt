package com.l0mtick.mgkcttimetable.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import com.l0mtick.mgkcttimetable.data.remote.dto.LessonUnion
import com.l0mtick.mgkcttimetable.data.remote.dto.Type
import com.l0mtick.mgkcttimetable.domain.model.schedule.DaySchedule
import com.l0mtick.mgkcttimetable.domain.model.schedule.Lesson
import com.l0mtick.mgkcttimetable.domain.model.schedule.ScheduleUnion
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleEvent
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDayCard(
    state: ScheduleState,
    onEvent: (ScheduleEvent) -> Unit
) {

    val daySchedule = DaySchedule(
        weekday = "Пятница",
        date = "07.01.2024",
        lessons = listOf(
            ScheduleUnion.LessonArrayValue(
                listOf(
                    Lesson(
                        subgroup = 1,
                        number = 1,
                        name = "Осн менеджмента",
                        type = Type.Fv.value,
                        teacher = "Усикова Л. Н.",
                        cabinet = "3-114",
                        comment = "2 часа"
                    ),
                    Lesson(
                        subgroup = 2,
                        number = 1,
                        name = "Осн менеджмента",
                        type = Type.Fv.value,
                        teacher = "Усикова Л. Н.",
                        cabinet = "3-114",
                        comment = "2 часа"
                    )
                )
            ),
            ScheduleUnion.LessonValue(
                Lesson(
                    subgroup = 1,
                    number = 1,
                    name = "Осн менеджмента",
                    type = Type.Fv.value,
                    teacher = "Усикова Л. Н.",
                    cabinet = "3-114",
                    comment = "2 часа"
                )
            )
        )
    )

    val firstLesson = daySchedule.lessons!![0]

    //TODO: rewrite highlighting of current lesson
    val currentLessonNumber = when (state.currentHour) {
        in 7..10 -> 1
        in 11..12 -> 2
        in 13..14 -> 3
        in 15..16 -> 4
        in 17..19 -> 5
        else -> 0
    }
    Card(
        onClick = {
            onEvent(ScheduleEvent.OnSpecificDayClick(daySchedule.date))
        },
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = Modifier
            .padding(20.dp),
//            .shadow(elevation = 4.dp),
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
                text = "${daySchedule.date}, ${daySchedule.weekday}",
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
                )
            }
        }
        AnimatedVisibility(visible = state.selectedDay == daySchedule.date) {
//            Column {
//                lessons.forEachIndexed { index, s ->
//                    val backgroundColor by animateColorAsState(
//                        targetValue = if (lessonNumbers.get(index)
//                                .toInt() == currentLessonNumber && state.currentDayOfWeek?.value == day + 1
//                        ) {
//                            Log.d(
//                                "timetableTest",
//                                "selected day - ${state.selectedDay}, card ${day + 1}"
//                            )
//                            MaterialTheme.colorScheme.tertiaryContainer
//                        } else {
//                            Color.Transparent
//                        }, label = "Backcolor animation for current lesson",
//                        animationSpec = tween(durationMillis = 700)
//                    )
//                    Row(
//                        modifier = Modifier
//                            .background(
//                                color = backgroundColor,
//                                shape = RoundedCornerShape(20.dp)
//                            )
//                            .padding(horizontal = 20.dp, vertical = 10.dp)
//                            .fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = "${lessonNumbers.get(index)}.",
//                            modifier = Modifier
//                                .padding(end = 10.dp),
//                            fontSize = 17.sp
//                        )
//                        Text(
//                            text = s.replace(") ", ")\n").replace("2.", "\n2."),
//                            modifier = Modifier
//                                .weight(1f)
//                        )
//                        Text(
//                            text = auditory.get(index).replace(" ", "\n"),
//                        )
//                    }
//                }
//            }
            Column {
                daySchedule.lessons.forEach { scheduleUnion ->
                    Row(
                        modifier = Modifier
                            .background(
                                color = Color.Transparent,
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
                                        .padding(end = 10.dp),
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
                                        .padding(end = 10.dp),
                                    fontSize = 18.sp
                                )
                                LessonRow(lesson = scheduleUnion.value)
                            }

                            null -> TODO()
                        }
                    }
//                    Divider(
//                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp).fillMaxWidth(),
//                        thickness = 1.dp,
//                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = .5f)
//                    )
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
        val mainString = "${lesson.subgroup ?: ""}. " +
            lesson.name.toString() +
                " (${lesson.type ?: ""})" +
                "\n${lesson.teacher}" +
                "  ${(lesson.comment ?: "")}"
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