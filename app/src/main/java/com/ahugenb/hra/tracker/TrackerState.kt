package com.ahugenb.hra.tracker

import com.ahugenb.hra.tracker.db.Day

sealed class TrackerState {
    class TrackerStateEmpty: TrackerState()

    class TrackerStateDay(val day: Day): TrackerState()
}
