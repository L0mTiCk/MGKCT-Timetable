package com.l0mtick.mgkcttimetable.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetable")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val groupName: String,
    val day: String,
    val lesson: String,
    val auditorium: String
)