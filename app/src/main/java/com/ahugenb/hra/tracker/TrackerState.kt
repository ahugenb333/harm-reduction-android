package com.ahugenb.hra.tracker

import com.ahugenb.hra.tracker.db.Day

sealed class TrackerState {
    class TrackerStateEmpty: TrackerState()
    data class TrackerStateAll(
        val today: Day,
        val selectedDay: Day = today,
        val selectedMonday: Day = today,
        val all: List<Day> = listOf(),
        val daysOfWeek: List<Day> = listOf(),
        val weekBeginnings: List<Day> = listOf()
    ): TrackerState()
}
