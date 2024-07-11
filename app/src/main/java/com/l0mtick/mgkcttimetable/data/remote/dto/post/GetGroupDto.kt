package com.l0mtick.mgkcttimetable.data.remote.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class GetGroupDto (
    val group: Int? = null
)
