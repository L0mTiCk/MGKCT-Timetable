package com.l0mtick.mgkcttimetable.data.remote.mappers

import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherDay
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherScheduleDto
import com.l0mtick.mgkcttimetable.domain.model.schedule.DaySchedule
import com.l0mtick.mgkcttimetable.domain.model.schedule.Lesson
import com.l0mtick.mgkcttimetable.domain.model.schedule.ScheduleUnion
import com.l0mtick.mgkcttimetable.domain.model.schedule.WeekSchedule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun TeacherScheduleDto.toWeekSchedule(): WeekSchedule {
    val scheduleList = mutableListOf<DaySchedule>()
    val changedDate = Date(response?.changed ?: 0L)
    val updateDate = Date(response?.update ?: 0L)
    val sdf = SimpleDateFormat("dd MMMM HH:mm:ss", Locale.getDefault())
    val changedFormatted = sdf.format(changedDate)
    val updateFormatted = sdf.format(updateDate)
    response?.days?.forEach {
        scheduleList.add(it.toDaySchedule())
    }
    return WeekSchedule(
        days = scheduleList,
        update = updateFormatted,
        changed = changedFormatted
    )
}

fun TeacherDay.toDaySchedule(): DaySchedule {
    val lessonList = mutableListOf<ScheduleUnion?>()
    lessons?.forEachIndexed { index, lesson ->
        when(lesson) {
            null -> {
                lessonList.add(null)
            }
            else -> {
                lessonList.add(
                    ScheduleUnion.LessonValue(
                        Lesson(
                            number = index + 1,
                            name = "${lesson.group} ${lesson.lesson}",
                            type = lesson.type,
                            cabinet = lesson.cabinet,
                            comment = lesson.comment
                        )
                    )
                )
            }
        }
    }
    return DaySchedule(
        weekday = weekday ?: "Null",
        date = day ?: "Null",
        lessons = lessonList
    )
}