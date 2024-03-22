package com.l0mtick.mgkcttimetable.data.repository

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.l0mtick.mgkcttimetable.R
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleApi
import com.l0mtick.mgkcttimetable.data.remote.mappers.toWeekSchedule
import com.l0mtick.mgkcttimetable.data.utils.Constants
import com.l0mtick.mgkcttimetable.domain.model.schedule.WeekSchedule
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Locale

private const val API_LOG = "api_test"

class ScheduleRepositoryImpl(
    private val activity: Activity,
    private val sharedPreferences: SharedPreferences,
//    private val scheduleDao: ScheduleDao,
    private val scheduleApi: ScheduleApi
) : ScheduleRepository {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var toast: Toast? = null

    override suspend fun parseGroupTimetable(groupNumber: String): WeekSchedule {
        delay(100)
        return withContext(Dispatchers.IO) {
            try {
                Log.d(API_LOG, "Parse group schedule, group - $groupNumber")
                val result = scheduleApi.getGroupSchedule(groupNumber).toWeekSchedule()
//                Log.d(API_LOG, result.toString())
                result
            } catch (e: Exception) {
                Log.e(API_LOG, e.toString())
                showApiError(e)
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
//                Log.d(API_LOG, result.toString())
                result
            } catch (e: Exception) {
                Log.e(API_LOG, e.toString())
                showApiError(e)
                WeekSchedule(null, "", "")
            }
        }
    }

    override suspend fun getDbGroupTimetable(mode: Int): List<Map<Int, List<String>>>? {
        TODO("Not yet implemented")
    }

    override suspend fun saveTimetableToDb(schedule: MutableMap<String, List<Map<Int, List<String>>>>) {
        TODO("Not yet implemented")
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

    override suspend fun enableNotifications(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                toast = Toast.makeText(
                    activity,
                    activity.getString(R.string.toast_notifications),
                    Toast.LENGTH_SHORT
                )
                toast?.show()
                return false
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            Firebase.messaging.subscribeToTopic(Constants.SCHEDULE_UPDATE).await()
            sharedPreferences.edit().putBoolean(Constants.ARE_NOTIFICATIONS_ENABLED, true).apply()
        }
        return true
    }

    override suspend fun disableNotifications() {
        sharedPreferences.edit().putBoolean(Constants.ARE_NOTIFICATIONS_ENABLED, false).apply()
        Firebase.messaging.unsubscribeFromTopic(Constants.SCHEDULE_UPDATE).await()
    }

    override fun getNotificationPermissionStatus(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            sharedPreferences.getBoolean(Constants.ARE_NOTIFICATIONS_ENABLED, true)
        ) {
            val hasPermission = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            hasPermission
        } else {
            sharedPreferences.getBoolean(Constants.ARE_NOTIFICATIONS_ENABLED, true)
        }
    }

    override fun getEmptySelectedString(): String {
        return activity.getString(R.string.screen_no_group)
    }

    override fun getCurrentLesson(): Int {
        val calendar = Calendar.getInstance()
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)

        val schedule: List<String> = if (LocalDateTime.now().dayOfWeek == DayOfWeek.SATURDAY)
            listOf("09:00", "10:50", "12:50", "14:40", "16:30", "18:20")
        else
            listOf("09:00", "10:50", "13:00", "14:50", "16:40", "18:30")

        var currentLesson = -1
        for (i in schedule.indices) {
            if (currentTime < schedule[i]) {
                currentLesson = i
                break
            }
        }
        Log.d(API_LOG, "Current lesson $currentLesson")
        return currentLesson
    }

    private fun showApiError(e: Exception) {
        activity.runOnUiThread {
            when (e) {
                is UnknownHostException, is SocketTimeoutException -> {
                    toast = Toast.makeText(
                        activity,
                        "Error with connecting to server",
                        Toast.LENGTH_SHORT
                    )
                    toast?.show()
                }

                else -> {
                    toast = Toast.makeText(
                        activity,
                        "Internal error",
                        Toast.LENGTH_SHORT
                    )
                    toast?.show()
                }
            }
        }
    }
}