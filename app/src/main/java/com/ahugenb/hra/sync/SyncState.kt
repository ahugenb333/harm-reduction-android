package com.ahugenb.hra.sync

import com.google.firebase.auth.FirebaseUser

sealed class SyncState {

    class SyncStateEmpty: SyncState()

    data class SyncStateAll(
        val firebaseUser: FirebaseUser? = null
    ): SyncState()
}