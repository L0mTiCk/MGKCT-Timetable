package com.l0mtick.mgkcttimetable.presentation.schedule

import java.time.DayOfWeek
import java.time.LocalDateTime

data class ScheduleState (
    val currentDayOfWeek: DayOfWeek? = LocalDateTime.now().dayOfWeek,
    val groupSchedule: List<Map<Int, List<String>>> = emptyList(),
    val isScheduleUpdating: Boolean = true,
    val selectedDay: Int = currentDayOfWeek!!.value,
    val selectedGroup: String = "",
    val currentHour: Int = LocalDateTime.now().hour
)