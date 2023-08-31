package com.l0mtick.mgkcttimetable.presentation.schedule

import java.time.DayOfWeek
import java.time.LocalDateTime

data class ScheduleState (
    val isConnected: Boolean = true,
    val currentDayOfWeek: DayOfWeek? = LocalDateTime.now().dayOfWeek,
    val groupSchedule: List<Map<Int, List<String>>> = listOf(emptyMap(), emptyMap(), emptyMap()),
    val isScheduleUpdating: Boolean = true,
    val selectedDay: Int = currentDayOfWeek!!.value,
    val selectedGroup: String = "",
    val currentHour: Int = LocalDateTime.now().hour
)