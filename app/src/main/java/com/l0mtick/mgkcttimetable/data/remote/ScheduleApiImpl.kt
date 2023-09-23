package com.l0mtick.mgkcttimetable.data.remote

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

private const val api_url = "https://mgke.keller.by/api"
private const val token = "b6xUMDWDrndJ2aIwVekKPX3NoDWfnQaFuKL38ZJ0efNTMw1FNzt4SQrJY-Lo-AB0Q1n9xA"
private val client = OkHttpClient()

class ScheduleApiImpl: ScheduleApi {
    override suspend fun getGroupSchedule(groupNumber: Int) {
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
            val schedule = GroupScheduleDto.fromJson(response.body?.string() ?: "")
        }
        //TODO: return schedule for group
    }

    override suspend fun getTeacherSchedule(teacher: String) {
        val json = "{\"teacher\": $teacher}"
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
            val schedule = TeacherScheduleDto.fromJson(response.body?.string() ?: "")
        }
    }

    override suspend fun getAllGroupsNumbers() {
        val request = Request.Builder()
            .url("$api_url/getGroups")
            .header(
                "Authorization",
                "Bearer $token"
            )
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            //TODO: all groups dto
        }
    }

    override suspend fun getAllTeacherNames(): String {
        val request = Request.Builder()
            .url("$api_url/getTeachers")
            .header(
                "Authorization",
                "Bearer $token"
            )
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            //TODO: all teachers dto
            return response.body?.string() ?: ""
        }
        return "Error"
    }
}