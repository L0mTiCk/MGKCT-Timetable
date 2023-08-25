package com.l0mtick.mgkcttimetable.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScheduleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timetableDao(): ScheduleDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            return INSTANCE
                ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .createFromAsset("database/user.db")
                        .build()
                    INSTANCE = instance
                    return instance
                }
        }
    }
}