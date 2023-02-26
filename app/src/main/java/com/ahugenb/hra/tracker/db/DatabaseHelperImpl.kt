package com.ahugenb.hra.tracker.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseHelperImpl(
    private val trackerDatabase: TrackerDatabase
): DatabaseHelper {

    override fun getDays(): Flow<List<Day>> = flow {
        emit(trackerDatabase.dayDao().getDays())
    }

    override fun insertAll(days: List<Day>): Flow<List<Day>> = flow {
        trackerDatabase.dayDao().insertDays(days)
        emit(days)
    }
}