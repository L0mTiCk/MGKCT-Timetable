package com.l0mtick.mgkcttimetable.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l0mtick.mgkcttimetable.presentation.ScheduleEvent
import com.l0mtick.mgkcttimetable.presentation.ScheduleState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDayCard(day: Int, lessons:  List<String>, auditory: List<String>, lessonNumbers: List<String>, state: ScheduleState, onEvent: (ScheduleEvent) -> Unit) {

    val currentLessonNumber = when (state.currentHour) {
        in 7..10 -> 1
        in 11..12 -> 2
        in 13..14 -> 3
        in 15..16 -> 4
        in 17..19 -> 5
        else -> 0
    }

    val rotationAngle by animateFloatAsState(
        targetValue = if (state.selectedDay == day + 1) 180f else 0f,
        label = "Rotate arrow"
    )

    Card(
        onClick = {
            onEvent(ScheduleEvent.OnSpecificDayClick(day + 1))
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
                text = getDayOfWeekNameAndNumber(day),
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            )
            IconButton(
                onClick = {
                    onEvent(ScheduleEvent.OnSpecificDayClick(day + 1))
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
        AnimatedVisibility(visible = state.selectedDay == day + 1) {
            Column {
                lessons.forEachIndexed { index, s ->
                    val backgroundColor by animateColorAsState(
                        targetValue = if (lessonNumbers.get(index).toInt() == currentLessonNumber && state.currentDayOfWeek?.value == day + 1) {
                            Log.d("timetableTest", "selected day - ${state.selectedDay}, card ${day + 1}")
                            MaterialTheme.colorScheme.tertiaryContainer
                        } else {
                            Color.Transparent
                        }, label = "Backcolor animation for current lesson",
                        animationSpec = tween(durationMillis = 700)
                    )
                    Row(
                        modifier = Modifier
                            .background(
                                color = backgroundColor,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .fillMaxWidth()
                            ,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${lessonNumbers.get(index)}.",
                            modifier = Modifier
                                .padding(end = 10.dp, bottom = 20.dp),
                            fontSize = 17.sp
                        )
                        Text(
                            text = s.replace(") ", ")\n").replace("2.", "\n2."),
                            modifier = Modifier
                                .weight(1f)
                        )
                        Text(
                            text = auditory.get(index).replace(" ", "\n"),
                        )
                    }
                }
            }
        }
    }
}

fun getDayOfWeekNameAndNumber(dayOfWeek: Int): String {
    val day = DayOfWeek.of(dayOfWeek + 1)
    val dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val currentDate = LocalDate.now()
    val currentDayOfWeek = currentDate.dayOfWeek.value
    val daysToAdd = dayOfWeek - currentDayOfWeek
    val date = currentDate.plusDays(daysToAdd.toLong())
    val dayNumber = date.dayOfMonth
    return "${dayNumber + 1}. ${dayName.replaceFirstChar { it.uppercase() }}"
}