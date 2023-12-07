package com.ahugenb.hra.goal.db

import android.content.Context
import androidx.room.Room

object GoalDatabaseBuilder {

    private var INSTANCE: GoalDatabase? = null

    fun getInstance(context: Context): GoalDatabase {
        if (INSTANCE == null) {
            synchronized(GoalDatabase::class) {
                if (INSTANCE == null) {
                    INSTANCE = buildRoomDB(context)
                }
            }
        }
        return INSTANCE!!
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            GoalDatabase::class.java,
            "Goal-database"
        ).build()
}