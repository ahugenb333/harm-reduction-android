package com.ahugenb.hra.sync

import android.content.Context
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepositoryImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): SyncRepository {

    override fun getFirebaseInfo(): Flow<Info> = flow {
        emit(AdvertisingIdClient.getAdvertisingIdInfo(applicationContext))
    }
}