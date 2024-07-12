package com.l0mtick.mgkcttimetable.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeacherScheduleDto (
    val response: TeacherResponse? = null
)

@Serializable
data class TeacherResponse (
    val days: List<TeacherDay>? = null,
    val update: Long? = null,
    val changed: Long? = null,
    val lastSuccess: Boolean? = null
)

@Serializable
data class TeacherDay (
    val weekday: String? = null,
    val day: String? = null,
    val lessons: List<TeacherLesson?>? = null
)

@Serializable
data class TeacherLesson (
    val lesson: String? = null,
    val type: String? = null,
    val group: String? = null,
    val cabinet: String? = null,
    val comment: String? = null
)
