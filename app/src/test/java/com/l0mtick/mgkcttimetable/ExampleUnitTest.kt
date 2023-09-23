package com.l0mtick.mgkcttimetable

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_api() {
        val api_url = "https://mgke.keller.by/api"

        val client = OkHttpClient()
        val json = "{\"group\": 60}" // Параметры запроса в формате JSON
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
//        val request = Request.Builder()
//            .url(api_url)
//            .post(requestBody)
//            .header(
//                "Authorization",
//                "Bearer b6xUMDWDrndJ2aIwVekKPX3NoDWfnQaFuKL38ZJ0efNTMw1FNzt4SQrJY-Lo-AB0Q1n9xA"
//            )
//            .build()
//        val response = client.newCall(request).execute()
//        println(response.body?.string())
//        if (response.isSuccessful) {
//            println("success")
//            val schedule = GroupResponse.fromJson(response.body?.string() ?: "")
//            for (day in schedule?.response?.days ?: emptyList()) {
//                println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
//                println("${day.day} ${day.weekday}")
//                for (lesson in day.lessons) {
//                    when(lesson) {
//                        is LessonUnion.LessonClassValue -> println(lesson.value)
//                        is LessonUnion.LessonClassArrayValue -> {
//                            for (lessonClass in lesson.value) {
//                                println(lessonClass)
//                            }
//                        }
//
//                        is LessonUnion.NullValue -> {
//                            println(null)
//                        }
//                    }
//                }
//            }
//        }
        val request = Request.Builder()
            .url("$api_url/getGroup")
            .post(requestBody)
            .header(
                "Authorization",
                "Bearer b6xUMDWDrndJ2aIwVekKPX3NoDWfnQaFuKL38ZJ0efNTMw1FNzt4SQrJY-Lo-AB0Q1n9xA"
            )
            .build()
        val response = client.newCall(request).execute()
        println(response.body?.string())
    }
}