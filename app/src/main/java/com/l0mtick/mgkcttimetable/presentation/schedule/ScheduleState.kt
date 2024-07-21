package com.l0mtick.mgkcttimetable.presentation.schedule

import com.l0mtick.mgkcttimetable.domain.model.schedule.WeekSchedule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ScheduleState (
    val isConnected: Boolean = true,
    val isScheduleUpdating: Boolean = true,
    val selectedDay: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
    val selectedGroup: String = "",
    val currentLesson: Int = 0,
    val groupSchedule: WeekSchedule = WeekSchedule(null, "", "")
)