package com.ahugenb.hra.tracker.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Hilt needs to know how to create this as a singleton if DayRepository is a Singleton
class DayRepositoryImpl @Inject constructor(
    private val dayDao: DayDao // Changed to DayDao
): DayRepository {

    override fun getDays(): Flow<List<Day>> = flow {
        emit(dayDao.getDays()) // Use dayDao directly
    }

    override fun insertDays(days: List<Day>): Flow<Unit> = flow {
        dayDao.insertDays(days)
        emit(Unit)
    }

    override fun updateDay(day: Day): Flow<Unit> = flow {
        dayDao.updateDay(day)
        emit(Unit)
    }
}