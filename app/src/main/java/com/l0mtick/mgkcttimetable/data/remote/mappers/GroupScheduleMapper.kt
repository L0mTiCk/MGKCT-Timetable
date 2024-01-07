package com.l0mtick.mgkcttimetable.data.remote.mappers

import com.l0mtick.mgkcttimetable.data.remote.dto.GroupDay
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupScheduleDto
import com.l0mtick.mgkcttimetable.data.remote.dto.LessonUnion
import com.l0mtick.mgkcttimetable.domain.model.schedule.DaySchedule
import com.l0mtick.mgkcttimetable.domain.model.schedule.Lesson
import com.l0mtick.mgkcttimetable.domain.model.schedule.ScheduleUnion
import com.l0mtick.mgkcttimetable.domain.model.schedule.WeekSchedule

//fun GroupScheduleDto.toWeekSchedule(): WeekSchedule {
//    response?.days?.forEach {  }
//    return WeekSchedule(
//
//    )
//}

fun GroupDay.toDaySchedule(): DaySchedule {
    val lessonList = mutableListOf<ScheduleUnion?>()
    lessons?.forEach {
        when (it) {
            is LessonUnion.LessonClassArrayValue -> {
                val resultName: String
//                val result
                it.value.forEach {

                }
            }
            is LessonUnion.LessonClassValue -> {
                lessonList.add(
                    ScheduleUnion.LessonValue(
                        Lesson(
                            name = it.value.lesson,
                            type = it.value.type?.value,
                            teacher = it.value.teacher,
                            cabinet = it.value.cabinet,
                            comment = it.value.comment
                        )
                    )
                )
            }
            is LessonUnion.NullValue -> {
                lessonList.add(null)
            }
        }
    }
    return DaySchedule(
        weekday = weekday ?: "Null",
        date = day ?: "Null",
        lessons = lessonList
    )
}