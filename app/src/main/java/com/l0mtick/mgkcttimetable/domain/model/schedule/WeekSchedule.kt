package com.l0mtick.mgkcttimetable.domain.model.schedule

import androidx.compose.runtime.Stable

@Stable
data class WeekSchedule(
    val days: List<DaySchedule>? = null,
    val update: String,
    val changed: String
)