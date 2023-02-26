package com.ahugenb.hra.tracker.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {
    @Query("SELECT * FROM days")
    fun getDays(): List<Day>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDays(days: List<Day>)

    @Update
    fun updateDay(day: Day)
}