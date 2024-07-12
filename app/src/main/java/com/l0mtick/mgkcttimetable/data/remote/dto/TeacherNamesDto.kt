package com.l0mtick.mgkcttimetable.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeacherNamesDto(
    val response: List<String>? = null
)