package com.l0mtick.mgkcttimetable.presentation.schedule

import com.l0mtick.mgkcttimetable.domain.model.schedule.WeekSchedule
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

data class ScheduleState (
    val isConnected: Boolean = true,
    val currentDayOfWeek: DayOfWeek? = LocalDateTime.now().dayOfWeek,
    val groupSchedule: List<Map<Int, List<String>>> = listOf(emptyMap(), emptyMap(), emptyMap()),
    val isScheduleUpdating: Boolean = true,
    val selectedDay: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
    val selectedGroup: String = "",
    val currentHour: Int = LocalDateTime.now().hour,
    //TODO: remove groupSchedule, rename this
    val tempGroupSchedule: WeekSchedule = WeekSchedule(null, Date(1101L), Date(1010L))
)