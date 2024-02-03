package com.l0mtick.mgkcttimetable.data.remote

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupNumbersDto
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupScheduleDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherNamesDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherScheduleDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import java.util.Properties

private const val api_url = "https://mgke.keller.by/api"
private val client = OkHttpClient()

class ScheduleApiImpl(private val token: String = ""): ScheduleApi {
    override suspend fun getGroupSchedule(groupNumber: String): GroupScheduleDto {
        if (checkRequestBody(groupNumber))
            return GroupScheduleDto(null)
        val json = "{\"group\": $groupNumber}"
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$api_url/getGroup")
            .post(requestBody)
            .header(
                "Authorization",
                "Bearer $token"
            )
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            try {
                val schedule = GroupScheduleDto.fromJson(response.body?.string() ?: "")
                return schedule ?: GroupScheduleDto(null)
            } catch (e: Exception) {
                Log.e("api_test", "Error while converting group schedule response: ${e}")
            }
        }
        return GroupScheduleDto(response = null)
    }

    override suspend fun getTeacherSchedule(teacher: String): TeacherScheduleDto {
        if (checkRequestBody(teacher))
            return TeacherScheduleDto(null)
        val json = "{\"teacher\": \"$teacher\"}"
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$api_url/getTeacher")
            .post(requestBody)
            .header(
                "Authorization",
                "Bearer $token"
            )
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val schedule: TeacherScheduleDto
            try {
                schedule = TeacherScheduleDto.fromJson(response.body?.string() ?: "")!!
                return schedule
            } catch (e: Exception) {
                Log.e("api_test", e.message.orEmpty())
            }
        }
        return TeacherScheduleDto(null)
    }

    override suspend fun getAllGroupsNumbers(): GroupNumbersDto {
        val request = Request.Builder()
            .url("$api_url/getGroups")
            .header(
                "Authorization",
                "Bearer $token"
            )
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            try {
                return GroupNumbersDto.fromJson(response.body?.string() ?: "null")!!
            } catch (e: Exception) {
                Log.e("api_test", "Error while converting all group numbers response")
            }
        }
        return GroupNumbersDto(emptyList())
    }

    override suspend fun getAllTeacherNames(): TeacherNamesDto {
        val request = Request.Builder()
            .url("$api_url/getTeachers")
            .header(
                "Authorization",
                "Bearer $token"
            )
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            try {
                return TeacherNamesDto.fromJson(response.body?.string() ?: "null")!!
            } catch (e: Exception) {
                Log.e("api_test", "Error while converting all teachers response")
            }
        }
        return TeacherNamesDto(response = null)
    }

    private fun checkRequestBody(string: String): Boolean {
        return string.contentEquals("Никто не выбран")
    }
}