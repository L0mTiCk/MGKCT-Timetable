package com.l0mtick.mgkcttimetable.domain.repository

import android.content.Context
import com.l0mtick.mgkcttimetable.domain.model.schedule.WeekSchedule

interface ScheduleRepository {
    suspend fun parseGroupTimetable(groupNumber: String): WeekSchedule

    suspend fun parseTeacherTimetable(teacher: String): WeekSchedule

    suspend fun getDbGroupTimetable(mode: Int): List<Map<Int, List<String>>>?

    suspend fun saveTimetableToDb(schedule: MutableMap<String, List<Map<Int, List<String>>>>)

    fun saveGroup(group: String)

    fun getSavedGroup(): String?

    fun saveTeacher(group: String)

    fun getSavedTeacher(): String?

    suspend fun getAllGroupNames(): List<Long>?

    suspend fun getAllTeacherNames(): List<String>?

    fun getConnectionStatus(callback: (Boolean) -> Unit)

    fun unsubscribeConnectionStatus()

    fun saveStartDestinationRoute(route: String)

    fun getSavedStartDestinationRoute(): String?

    suspend fun enableNotifications(): Boolean

    suspend fun disableNotifications()

    fun getNotificationPermissionStatus(): Boolean

    fun getEmptySelectedString(): String

    fun getCurrentLesson(): Int

    suspend fun saveWidgetSelection(isGroup: Boolean)

    fun isWidgetGroupSelected(): Boolean

    suspend fun parseWidgetTimetable(): WeekSchedule

    suspend fun getWidgetHeader(): String
}