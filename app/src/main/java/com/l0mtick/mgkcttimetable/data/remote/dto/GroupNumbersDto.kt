package com.l0mtick.mgkcttimetable.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GroupNumbersDto(
    val response: List<Long>? = null
)