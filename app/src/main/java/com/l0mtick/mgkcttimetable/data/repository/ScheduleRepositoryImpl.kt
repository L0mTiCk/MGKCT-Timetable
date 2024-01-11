package com.l0mtick.mgkcttimetable.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.ContextCompat
import com.l0mtick.mgkcttimetable.data.remote.ScheduleApi
import com.l0mtick.mgkcttimetable.data.remote.mappers.toWeekSchedule
import com.l0mtick.mgkcttimetable.data.utils.Constants
import com.l0mtick.mgkcttimetable.domain.model.schedule.WeekSchedule
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val API_LOG = "api_test"

class ScheduleRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
//    private val scheduleDao: ScheduleDao,
    private val scheduleApi: ScheduleApi
) : ScheduleRepository {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override suspend fun parseGroupTimetable(groupNumber: String): WeekSchedule {
        delay(100)
        return withContext(Dispatchers.IO) {
            try {
                Log.d(API_LOG, "Parse group schedule, group - $groupNumber")
                val result = scheduleApi.getGroupSchedule(groupNumber).toWeekSchedule()
                Log.d(API_LOG, result.toString())
                result
            } catch (e: Exception) {
                Log.e(API_LOG, e.toString())
                WeekSchedule(null, "", "")
            }
        }
    }

    override suspend fun parseTeacherTimetable(teacher: String): WeekSchedule {
        delay(200)
        return withContext(Dispatchers.IO) {
            try {
                Log.d(API_LOG, "Parse teacher schedule, teacher - $teacher")
                val result = scheduleApi.getTeacherSchedule(teacher).toWeekSchedule()
                Log.d(API_LOG, result.toString())
                result
            } catch (e: Exception) {
                Log.e(API_LOG, e.toString())
                WeekSchedule(null, "", "")
            }
        }
    }

    override suspend fun getDbGroupTimetable(mode: Int): List<Map<Int, List<String>>>? {
//        val groupName = getSavedGroup()
//        val teacherName = getSavedTeacher()
//        when (mode) {
//            0 -> {
//                return if (groupName != null)
//                    scheduleDao.getScheduleForGroup(groupName)?.schedule
//                else
//                    null
//            }
//
//            1 -> {
//                return if (teacherName != null)
//                    scheduleDao.getScheduleForGroup(teacherName)?.schedule
//                else
//                    null
//            }
//
//            else -> return null
//        }
        return null
    }

    override suspend fun saveTimetableToDb(schedule: MutableMap<String, List<Map<Int, List<String>>>>) {
//        for (key in schedule.keys) {
//            CoroutineScope(Dispatchers.IO).launch {
//                val existingScheduleEntity = scheduleDao.getScheduleForGroup(key)
//                if (existingScheduleEntity != null) {
//                    // Update the existing entity
//                    existingScheduleEntity.schedule = schedule[key]!!
//                    scheduleDao.updateSchedule(existingScheduleEntity)
//                } else {
//                    // Insert a new entity
//                    val newScheduleEntity =
//                        ScheduleEntity(groupName = key, schedule = schedule[key]!!)
//                    scheduleDao.insertSchedule(newScheduleEntity)
//                }
//            }
//        }
        Log.d("timetable_parser", "Saved to DB")
    }

    override fun saveGroup(group: String) {
        sharedPreferences.edit().putString(Constants.SAVE_GROUP, group).apply()
    }

    override fun getSavedGroup(): String? {
        return sharedPreferences.getString(Constants.SAVE_GROUP, null)
    }

    override fun saveTeacher(group: String) {
        sharedPreferences.edit().putString(Constants.SAVE_TEACHER, group).apply()
    }

    override fun getSavedTeacher(): String? {
        return sharedPreferences.getString(Constants.SAVE_TEACHER, null)
    }

    override suspend fun getAllGroupNames(): List<Long>? {
        return withContext(Dispatchers.IO) {
            try {
                scheduleApi.getAllGroupsNumbers().response
            } catch (e: Exception) {
                Log.e(API_LOG, "Error while getting all groups: $e")
                emptyList()
            }
        }
    }

    override suspend fun getAllTeacherNames(): List<String>? {
        return withContext(Dispatchers.IO) {
            try {
                scheduleApi.getAllTeacherNames().response
            } catch (e: Exception) {
                Log.e(API_LOG, "Error while getting all teachers: $e")
                emptyList()
            }
        }
    }

    override fun getConnectionStatus(context: Context, callback: (Boolean) -> Unit) {
        try {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager = ContextCompat.getSystemService(
                context,
                ConnectivityManager::class.java
            ) as ConnectivityManager
            networkCallback = object : ConnectivityManager.NetworkCallback() {
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
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            val isWifiConnected =
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
            val isMobileConnected =
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
            callback(isMobileConnected || isWifiConnected)
        } catch (e: Exception) {
            Log.e("timetableTest", e.toString())
        }
    }

    override fun unsubscribeConnectionStatus() {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {
            Log.e("timetableTest", e.toString())
        }
    }

    override fun saveStartDestinationRoute(route: String) {
        sharedPreferences.edit().putString(Constants.SAVE_ROUTE, route).apply()
    }

    override fun getSavedStartDestinationRoute(): String? {
        return sharedPreferences.getString(Constants.SAVE_ROUTE, "group")
    }
}