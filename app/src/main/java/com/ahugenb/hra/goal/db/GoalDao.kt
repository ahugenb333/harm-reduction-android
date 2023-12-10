package com.ahugenb.hra.goal.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GoalDao {
    @Query("SELECT * FROM Goals")
    fun getGoals(): List<GoalImpl>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGoals(goals: List<GoalImpl>)
}