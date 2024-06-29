package com.l0mtick.mgkcttimetable.data.remote.dto

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

data class TeacherNamesDto(
    val response: List<String>? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String): TeacherNamesDto {
            try {
                val dto = klaxon.parse<TeacherNamesDto>(json)
                if (dto != null)
                    return dto
                else
                    return TeacherNamesDto(null)
            } catch (e: Exception) {
                return TeacherNamesDto(null)
            }
        }
    }
}