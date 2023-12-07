package com.ahugenb.hra.tracker.db

import android.content.Context
import androidx.room.Room

object TrackerDatabaseBuilder {

    private var INSTANCE: TrackerDatabase? = null

    fun getInstance(context: Context): TrackerDatabase {
        if (INSTANCE == null) {
            synchronized(TrackerDatabase::class) {
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
            TrackerDatabase::class.java,
            "tracker-database"
        ).build()
}