package com.ahugenb.hra.goal.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GoalEntity::class], version = 1)
abstract class GoalDatabase: RoomDatabase() {

    abstract fun goalDao(): GoalDao
}