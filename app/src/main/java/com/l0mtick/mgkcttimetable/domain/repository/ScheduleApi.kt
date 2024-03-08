package com.l0mtick.mgkcttimetable.domain.repository

import com.l0mtick.mgkcttimetable.data.remote.dto.GroupNumbersDto
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupScheduleDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherNamesDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherScheduleDto

interface ScheduleApi {
    suspend fun getGroupSchedule(groupNumber: String): GroupScheduleDto
    suspend fun getTeacherSchedule(teacher: String): TeacherScheduleDto
    suspend fun getAllGroupsNumbers(): GroupNumbersDto
    suspend fun getAllTeacherNames(): TeacherNamesDto
}