package com.l0mtick.mgkcttimetable.data.remote

import com.l0mtick.mgkcttimetable.data.remote.dto.GroupNumbersDto
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupScheduleDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherNamesDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherScheduleDto

interface ScheduleApi {
    suspend fun getGroupSchedule(groupNumber: Int): GroupScheduleDto
    suspend fun getTeacherSchedule(teacher: String): TeacherScheduleDto
    suspend fun getAllGroupsNumbers(): GroupNumbersDto
    suspend fun getAllTeacherNames(): TeacherNamesDto
}