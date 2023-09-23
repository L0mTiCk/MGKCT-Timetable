package com.l0mtick.mgkcttimetable.data.remote

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

private const val api_url = "https://mgke.keller.by/api/"

interface ScheduleApi {

    suspend fun getGroupSchedule(groupNumber: Int)
    suspend fun getTeacherSchedule(teacher: String)
    suspend fun getAllGroupsNumbers()
    suspend fun getAllTeacherNames()
}

val client = OkHttpClient()

val request = Request.Builder()
    .url(api_url)
    .method("getGroup", "{\"group\": 60}".toRequestBody())
    .header("Authorization", "Bearer b6xUMDWDrndJ2aIwVekKPX3NoDWfnQaFuKL38ZJ0efNTMw1FNzt4SQrJY-Lo-AB0Q1n9xA")
    .build()


fun testApi() {
    val response = client.newCall(request).execute()
    val t = Gson().fromJson(response.body?.string(), GroupScheduleDto::class.java)
    for (day in t.response.days) {
        println(day)
    }
}