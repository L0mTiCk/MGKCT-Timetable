package com.l0mtick.mgkcttimetable.domain.model.schedule

import androidx.compose.runtime.Stable

@Stable
sealed class ScheduleUnion {
    class LessonValue(val value: Lesson): ScheduleUnion()
    class LessonArrayValue(val value: List<Lesson>): ScheduleUnion()
}