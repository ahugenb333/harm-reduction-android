package com.ahugenb.hra

import android.app.Application
import com.ahugenb.hra.tracker.db.DatabaseBuilder
import com.ahugenb.hra.tracker.db.DayRepositoryImpl

class HraApplication: Application() {
    val database by lazy { DatabaseBuilder.getInstance(this) }
    val dayRepository by lazy { DayRepositoryImpl(database) }
}