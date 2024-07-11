package com.l0mtick.mgkcttimetable.data.remote

import android.util.Log
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupNumbersDto
import com.l0mtick.mgkcttimetable.data.remote.dto.GroupScheduleDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherNamesDto
import com.l0mtick.mgkcttimetable.data.remote.dto.TeacherScheduleDto
import com.l0mtick.mgkcttimetable.data.remote.dto.post.GetGroupDto
import com.l0mtick.mgkcttimetable.data.remote.dto.post.GetTeacherDto
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ScheduleApiImpl(private val httpClient: HttpClient): ScheduleApi {

    override suspend fun getGroupSchedule(groupNumber: String): GroupScheduleDto {
        if (checkRequestBody(groupNumber))
            return GroupScheduleDto(null)
        val response = try {
            httpClient.post(
                urlString = "getGroup"
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    GetGroupDto(groupNumber.toInt())
                )
            }
        } catch (e: Exception) {
            return GroupScheduleDto(null)
        }
        if (response.status.isSuccess()) {
            try {
                return response.body<GroupScheduleDto>()
            } catch (e: Exception) {
                Log.e("api_test", "Error while converting group schedule response: ${e}")
            }
        }
        return GroupScheduleDto(response = null)
    }

    override suspend fun getTeacherSchedule(teacher: String): TeacherScheduleDto {
        if (checkRequestBody(teacher))
            return TeacherScheduleDto(null)
        val response = try {
            httpClient.post(
                urlString = "getTeacher"
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    GetTeacherDto(teacher)
                )
            }
        } catch (e: Exception) {
            return TeacherScheduleDto(null)
        }
        if (response.status.isSuccess()) {
            try {
                return response.body<TeacherScheduleDto>()
            } catch (e: Exception) {
                Log.e("api_test", e.message.orEmpty())
            }
        }
        return TeacherScheduleDto(null)
    }

    override suspend fun getAllGroupsNumbers(): GroupNumbersDto {
        val response = try {
            httpClient.get(
                urlString = "getGroups"
            )
        } catch (e: Exception) {
            return GroupNumbersDto(null)
        }
        if (response.status.isSuccess()) {
            try {
                return response.body<GroupNumbersDto>()
            } catch (e: Exception) {
                Log.e("api_test", "Error while converting all group numbers response")
            }
        }
        return GroupNumbersDto(emptyList())
    }

    override suspend fun getAllTeacherNames(): TeacherNamesDto {
        val response = try {
            httpClient.get(
                urlString = "getTeachers"
            )
        } catch (e: Exception) {
            return TeacherNamesDto(null)
        }
        if (response.status.isSuccess()) {
            try {
                return response.body<TeacherNamesDto>()
            } catch (e: Exception) {
                Log.e("api_test", "Error while converting all teachers response")
            }
        }
        return TeacherNamesDto(response = null)
    }

    private fun checkRequestBody(string: String): Boolean {
        return string.contains("Никто не выбран")
    }
}