package com.l0mtick.mgkcttimetable.domain.model.schedule

import com.l0mtick.mgkcttimetable.data.remote.dto.LessonUnion

sealed class ScheduleUnion {
    class LessonValue(val value: Lesson): ScheduleUnion()
    class LessonArrayValue(val value: List<Lesson>): ScheduleUnion()
}