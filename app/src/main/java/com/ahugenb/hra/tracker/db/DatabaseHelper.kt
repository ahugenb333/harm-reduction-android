package com.ahugenb.hra.tracker.db

import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {

    fun getDays(): Flow<List<Day>>

    fun insertAll(days: List<Day>): Flow<Unit>

    fun updateDay(day: Day): Flow<Unit>
}