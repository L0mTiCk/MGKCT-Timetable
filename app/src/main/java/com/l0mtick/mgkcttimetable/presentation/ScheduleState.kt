package com.l0mtick.mgkcttimetable.presentation

import java.time.DayOfWeek
import java.time.LocalDate


data class ScheduleState (
    val currentDayOfWeek: DayOfWeek? = LocalDate.now().dayOfWeek,
    val groupSchedule: List<Map<Int, List<String>>> = emptyList(),
    val isScheduleUpdating: Boolean = true,
    val isSelectedDayOpen: Boolean = false,
    val selectedDay: Int = currentDayOfWeek!!.value,
    val selectedGroup: String = ""
)