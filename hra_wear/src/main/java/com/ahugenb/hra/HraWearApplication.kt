package com.ahugenb.hra

import android.app.Application

class HraWearApplication: Application() {
    val wearRepository by lazy { WearRepositoryImpl(this) }
}