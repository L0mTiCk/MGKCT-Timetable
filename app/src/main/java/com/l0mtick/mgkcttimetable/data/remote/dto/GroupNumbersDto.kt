package com.l0mtick.mgkcttimetable.data.remote.dto

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

data class GroupNumbersDto(
    val response: List<Long>? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String): GroupNumbersDto {
            try {
                val dto = klaxon.parse<GroupNumbersDto>(json)
                if (dto != null)
                    return dto
                else
                    return GroupNumbersDto(null)
            } catch (e: Exception) {
                return GroupNumbersDto(null)
            }
        }
    }
}