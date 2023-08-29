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
            val existingScheduleEntity = scheduleDao.getScheduleForGroup(key)
            if (existingScheduleEntity != null) {
                // Update the existing entity
                existingScheduleEntity.schedule = schedule[key]!!
                scheduleDao.updateSchedule(existingScheduleEntity)
            } else {
                // Insert a new entity
                val newScheduleEntity = ScheduleEntity(groupName = key, schedule = schedule[key]!!)
                scheduleDao.insertSchedule(newScheduleEntity)
            }
        }
    }

    override fun saveGroup(group: String) {
        when (group.length) {
            in 1..6 -> sharedPreferences.edit().putString(SAVE_GROUP, "Группа - $group").apply()
            else -> sharedPreferences.edit().putString(SAVE_GROUP, group).apply()
        }
    }

    override fun getSavedGroup(): String? {
        return sharedPreferences.getString(SAVE_GROUP, null)
    }

    override suspend fun getAllGroupNames(): List<String>? {
        return scheduleDao.getAllGroupNames()
    }


}