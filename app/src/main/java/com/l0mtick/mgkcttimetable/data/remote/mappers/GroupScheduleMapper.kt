package com.l0mtick.mgkcttimetable.data.remote.mappers

import com.l0mtick.mgkcttimetable.data.remote.dto.GroupDay
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupScheduleDto
import com.l0mtick.mgkcttimetable.data.remote.dto.LessonUnion
import com.l0mtick.mgkcttimetable.domain.model.schedule.DaySchedule
import com.l0mtick.mgkcttimetable.domain.model.schedule.Lesson
import com.l0mtick.mgkcttimetable.domain.model.schedule.ScheduleUnion
import com.l0mtick.mgkcttimetable.domain.model.schedule.WeekSchedule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun GroupScheduleDto.toWeekSchedule(): WeekSchedule {
    val scheduleList = mutableListOf<DaySchedule>()
    val changedDate = Date(response?.changed ?: 0L)
    val updateDate = Date(response?.update ?: 0L)
    val sdf = SimpleDateFormat("dd MMMM HH:mm:ss", Locale.getDefault())
    val changedFormatted = sdf.format(changedDate)
    val updateFormatted = sdf.format(updateDate)
    response?.days?.forEach {
        scheduleList.add(
            it.toDaySchedule()
        )
    }
    return WeekSchedule(
        days = scheduleList,
        changed = changedFormatted,
        update = updateFormatted
    )
}

fun GroupDay.toDaySchedule(): DaySchedule {
    val lessonList = mutableListOf<ScheduleUnion?>()
    lessons?.forEachIndexed { index, it ->
        when (it) {
            is LessonUnion.LessonClassArrayValue -> {
                val tempList = mutableListOf<Lesson>()
                it.value.forEach {  lessonClass ->
                    tempList.add(
                        Lesson(
                            number = index + 1,
                            subgroup = lessonClass.subgroup?.toInt(),
                            name = lessonClass.lesson,
                            type = lessonClass.type?.value,
                            teacher = lessonClass.teacher,
                            cabinet = lessonClass.cabinet,
                            comment = lessonClass.comment
                        )
                    )
                }
                val lessonArrayUnion = ScheduleUnion.LessonArrayValue(tempList)
                lessonList.add(lessonArrayUnion)
            }
            is LessonUnion.LessonClassValue -> {
                lessonList.add(
                    ScheduleUnion.LessonValue(
                        Lesson(
                            number = index + 1,
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