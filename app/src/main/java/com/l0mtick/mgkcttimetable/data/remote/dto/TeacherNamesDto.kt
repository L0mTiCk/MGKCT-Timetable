package com.l0mtick.mgkcttimetable.data.remote.dto

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

data class TeacherNamesDto (
    val response: List<String>? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<TeacherNamesDto>(json)
    }
}