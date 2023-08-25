package com.l0mtick.mgkcttimetable.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Query("SELECT * FROM timetable WHERE groupName = :groupName")
    suspend fun getScheduleForGroup(groupName: String): ScheduleEntity?
}