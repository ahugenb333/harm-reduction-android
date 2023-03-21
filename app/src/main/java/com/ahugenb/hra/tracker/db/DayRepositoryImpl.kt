package com.ahugenb.hra.tracker.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DayRepositoryImpl(
    private val trackerDatabase: TrackerDatabase
): DayRepository {

    override fun getDays(): Flow<List<Day>> = flow {
        emit(trackerDatabase.dayDao().getDays())
    }

    override fun insertDays(days: List<Day>): Flow<Unit> = flow {
        trackerDatabase.dayDao().insertDays(days)
        emit(Unit)
    }

    override fun updateDay(day: Day): Flow<Unit> = flow {
        trackerDatabase.dayDao().updateDay(day)
        emit(Unit)
    }
}