package com.ahugenb.hra.sync

import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info

sealed class SyncState {

    class SyncStateEmpty: SyncState()

    data class SyncStateAll(
        val firebaseInfo: Info
    ): SyncState()
}