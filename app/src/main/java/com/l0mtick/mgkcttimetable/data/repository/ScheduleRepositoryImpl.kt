package com.l0mtick.mgkcttimetable.data.repository

import android.content.SharedPreferences
import com.l0mtick.mgkcttimetable.data.database.ScheduleDao
import com.l0mtick.mgkcttimetable.data.database.ScheduleEntity
import com.l0mtick.mgkcttimetable.data.remote.parseRawTimetable
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository

private const val SAVE_GROUP = "saved_group"

class ScheduleRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val scheduleDao: ScheduleDao
): ScheduleRepository {
    override suspend fun parseTimetable(): MutableMap<String, List<Map<Int, List<String>>>>? {
        val schedule = parseRawTimetable()
        if (schedule != null)
            saveTimetableToDb(schedule)
        return schedule
    }
    
    override suspend fun getDbGroupTimetable(): List<Map<Int, List<String>>>? {
        val groupName = getSavedGroup()
        return if (groupName != null)
            scheduleDao.getScheduleForGroup(groupName)?.schedule
        else
            null
    }

    override suspend fun saveTimetableToDb(schedule: MutableMap<String, List<Map<Int, List<String>>>>) {
        for (key in schedule.keys) {
            val entity = schedule[key]?.let { ScheduleEntity(groupName = key, schedule = it) }
            if (entity?.schedule != null)
                scheduleDao.insertSchedule(entity)
        }
    }

    override fun saveGroup(group: String) {
        sharedPreferences.edit().putString(SAVE_GROUP, "Группа - $group").apply()
    }

    override fun getSavedGroup(): String? {
        return sharedPreferences.getString(SAVE_GROUP, null)
    }
}