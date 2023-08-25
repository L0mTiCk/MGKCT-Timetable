package com.l0mtick.mgkcttimetable.presentation

 sealed interface StudentEvent {
     object OnNewLessonsParsed: StudentEvent
     data class OnSpecificDayClick(val id: Int): StudentEvent
 }