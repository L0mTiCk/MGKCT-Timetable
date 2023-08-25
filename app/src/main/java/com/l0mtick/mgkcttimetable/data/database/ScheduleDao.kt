package com.l0mtick.mgkcttimetable.data.database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insert(entry: ScheduleEntity)

    // Define other database operations as needed
}