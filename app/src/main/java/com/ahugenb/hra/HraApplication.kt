package com.ahugenb.hra

import android.app.Application
import com.ahugenb.hra.goal.db.GoalDatabaseBuilder
import com.ahugenb.hra.goal.db.GoalRepositoryImpl
import com.ahugenb.hra.sync.SyncRepositoryImpl
import com.ahugenb.hra.tracker.db.TrackerDatabaseBuilder
import com.ahugenb.hra.tracker.db.DayRepositoryImpl

class HraApplication: Application() {
    private val trackerDatabase by lazy { TrackerDatabaseBuilder.getInstance(this) }
    private val goalDatabase by lazy { GoalDatabaseBuilder.getInstance(this) }
    val dayRepository by lazy { DayRepositoryImpl(trackerDatabase) }
    val goalRepository by lazy { GoalRepositoryImpl(goalDatabase) }
    val syncRepository by lazy { SyncRepositoryImpl(this.applicationContext) }
}