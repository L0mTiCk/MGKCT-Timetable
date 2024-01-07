package com.l0mtick.mgkcttimetable.presentation.schedule

sealed interface ScheduleEvent {
    object UpdateSchedule: ScheduleEvent
    data class OnSpecificDayClick(val date: String): ScheduleEvent
    object OnUpdatingStart: ScheduleEvent
    object OnUpdatingFinished: ScheduleEvent
    data class OnNetworkChange(val isConnected: Boolean): ScheduleEvent
}