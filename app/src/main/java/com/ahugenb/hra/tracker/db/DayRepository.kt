package com.ahugenb.hra.tracker.db

import kotlinx.coroutines.flow.Flow

interface DayRepository {

    fun getDays(): Flow<List<Day>>

    fun insertDays(days: List<Day>): Flow<Unit>

    fun updateDay(day: Day): Flow<Unit>
}