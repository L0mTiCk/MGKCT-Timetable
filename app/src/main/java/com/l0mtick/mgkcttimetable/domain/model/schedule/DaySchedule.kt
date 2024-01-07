package com.l0mtick.mgkcttimetable.domain.model.schedule

import androidx.compose.runtime.Stable

@Stable
data class DaySchedule(
    val weekday: String = "",
    val date: String = "",
    val lessons: List<ScheduleUnion?>? = null
)