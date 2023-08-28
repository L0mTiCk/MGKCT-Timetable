package com.l0mtick.mgkcttimetable.presentation.schedule

sealed interface ScheduleEvent {
    object OnFirstLoad: ScheduleEvent
    object OnNewLessonsParsed: ScheduleEvent
    data class OnSpecificDayClick(val id: Int): ScheduleEvent
}