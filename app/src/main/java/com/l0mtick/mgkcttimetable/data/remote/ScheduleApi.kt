package com.l0mtick.mgkcttimetable.data.remote

interface ScheduleApi {
    suspend fun getGroupSchedule(groupNumber: Int)
    suspend fun getTeacherSchedule(teacher: String)
    suspend fun getAllGroupsNumbers()
    suspend fun getAllTeacherNames(): String
}