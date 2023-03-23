package com.ahugenb.hra.tracker

import com.ahugenb.hra.tracker.db.Day

sealed class TrackerState {
    class TrackerStateEmpty: TrackerState()
    class TrackerStateAll(
        val today: Day,
        val all: List<Day> = listOf(),
        val thisWeek: List<Day> = listOf(),
        val weekBeginnings: List<Day> = listOf()
        ): TrackerState()
}
