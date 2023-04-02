package com.ahugenb.hra.tracker.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Day::class], version = 1)
abstract class TrackerDatabase: RoomDatabase() {
    abstract fun dayDao(): DayDao
}