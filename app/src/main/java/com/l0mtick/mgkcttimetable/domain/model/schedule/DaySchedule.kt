package com.l0mtick.mgkcttimetable.domain.model.schedule

data class DaySchedule(
    val weekday: String = "",
    val date: String = "",
    val lessons: List<ScheduleUnion?>? = null
)