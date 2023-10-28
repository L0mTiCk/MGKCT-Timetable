package com.l0mtick.mgkcttimetable.data.remote

import com.l0mtick.mgkcttimetable.data.remote.dto.GroupNumbersDto

interface ScheduleApi {
    suspend fun getGroupSchedule(groupNumber: Int): String
    suspend fun getTeacherSchedule(teacher: String)
    suspend fun getAllGroupsNumbers(): GroupNumbersDto
    suspend fun getAllTeacherNames(): String
}