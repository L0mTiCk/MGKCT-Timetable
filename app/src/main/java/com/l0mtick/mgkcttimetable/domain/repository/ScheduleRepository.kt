package com.l0mtick.mgkcttimetable.domain.repository

interface ScheduleRepository {
    suspend fun parseTimetable(): MutableMap<String, List<Map<Int, List<String>>>>?

    suspend fun getDbGroupTimetable(): List<Map<Int, List<String>>>?

    suspend fun saveTimetableToDb(schedule: MutableMap<String, List<Map<Int, List<String>>>>)

    fun saveGroup(group: String)

    fun getSavedGroup(): String?

    suspend fun getAllGroupNames(): List<String>?
}