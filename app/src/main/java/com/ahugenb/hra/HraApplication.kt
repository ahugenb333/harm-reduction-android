package com.ahugenb.hra

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HraApplication: Application() {
    // Repositories will be provided by Hilt modules
    // and injected into ViewModels directly.
    // No need to manually instantiate them here.
}