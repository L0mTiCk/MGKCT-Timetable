package com.l0mtick.mgkcttimetable.domain.model.schedule

import androidx.compose.runtime.Stable

@Stable
data class Lesson(
    val number: Int? = null,
    val subgroup: Int? = null,
    val name: String? = "",
    val type: String? = "",
    val teacher: String? = "",
    val cabinet: String? = null,
    val comment: String? = null
)