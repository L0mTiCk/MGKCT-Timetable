package com.l0mtick.mgkcttimetable.data.remote

import android.util.Log
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupNumbersDto
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupScheduleDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherNamesDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherScheduleDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

private const val api_url = "https://mgke.keller.by/api"
private const val token = "b6xUMDWDrndJ2aIwVekKPX3NoDWfnQaFuKL38ZJ0efNTMw1FNzt4SQrJY-Lo-AB0Q1n9xA"
private val client = OkHttpClient()

class ScheduleApiImpl: ScheduleApi {
    override suspend fun getGroupSchedule(groupNumber: String): GroupScheduleDto {
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
}