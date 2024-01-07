package com.l0mtick.mgkcttimetable.domain.model.schedule

import java.util.Date

data class WeekSchedule(
    val days: List<DaySchedule>? = null,
    val update: Date,
    val changed: Date
)