package com.l0mtick.mgkcttimetable.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat
import com.l0mtick.mgkcttimetable.data.database.ScheduleDao
import com.l0mtick.mgkcttimetable.data.database.ScheduleEntity
import com.l0mtick.mgkcttimetable.data.remote.parseRawTimetable
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val SAVE_GROUP = "saved_group"
private const val SAVE_TEACHER = "saved_teacher"


class ScheduleRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val scheduleDao: ScheduleDao
): ScheduleRepository {
    override suspend fun parseTimetable(): MutableMap<String, List<Map<Int, List<String>>>>? {
        val schedule = parseRawTimetable()
        if (schedule != null)
            CoroutineScope(Dispatchers.IO).launch {
                saveTimetableToDb(schedule)
            }
        return schedule
    }
    
    override suspend fun getDbGroupTimetable(mode: Int): List<Map<Int, List<String>>>? {
        val groupName = getSavedGroup()
        val teacherName = getSavedTeacher()
        when(mode) {
            0 -> {
                return if (groupName != null)
                    scheduleDao.getScheduleForGroup(groupName)?.schedule
                else
                    null
            }
            1 -> {
                return if (teacherName != null)
                    scheduleDao.getScheduleForGroup(teacherName)?.schedule
                else
                    null
            }
            else -> return null
        }
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

    override fun saveTeacher(group: String) {
        sharedPreferences.edit().putString(SAVE_TEACHER, group).apply()
    }

    override fun getSavedTeacher(): String? {
        return sharedPreferences.getString(SAVE_TEACHER, null)
    }

    override suspend fun getAllGroupNames(): List<String>? {
        return scheduleDao.getAllNames()?.filter { it.contains("Группа") }
    }

    override suspend fun getAllTeacherNames(): List<String>? {
        return scheduleDao.getAllNames()?.filter { it.contains("Преподаватель") }
    }

    override fun getConnectionStatus(context: Context, callback: (Boolean) -> Unit) {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        val connectivityManager = ContextCompat.getSystemService(
            context,
            ConnectivityManager::class.java
        ) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                callback(true)
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                callback(false)
            }
        }
        connectivityManager.requestNetwork(networkRequest, networkCallback)
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isWifiConnected = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        val isMobileConnected = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        callback(isMobileConnected || isWifiConnected)
    }
}