package com.ahugenb.hra.sync

import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.flow.Flow

interface SyncRepository {
    fun getFirebaseInfo(): Flow<AdvertisingIdClient.Info>
}