package com.ahugenb.hra.sync

import android.content.Context
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncRepositoryImpl(
    private val applicationContext: Context
): SyncRepository {

    override fun getFirebaseInfo(): Flow<Info> = flow {
        emit(AdvertisingIdClient.getAdvertisingIdInfo(applicationContext))
    }
}