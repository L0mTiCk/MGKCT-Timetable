package com.l0mtick.mgkcttimetable.presentation

 sealed interface ScheduleEvent {
     object OnFirstLoad: ScheduleEvent
     object OnNewLessonsParsed: ScheduleEvent
     data class OnSpecificDayClick(val id: Int): ScheduleEvent
 }