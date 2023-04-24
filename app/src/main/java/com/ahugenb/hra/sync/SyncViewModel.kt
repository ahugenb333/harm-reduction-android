package com.ahugenb.hra.sync

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SyncViewModel: ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.SyncStateEmpty())
    val syncState: StateFlow<SyncState> = _syncState

    fun setUser(user: FirebaseUser?) {
        Log.d("SyncViewModel", "setUser: $user")
        _syncState.value = SyncState.SyncStateAll(user)
    }

    private fun fetchSheet() {
        if (_syncState.value is SyncState.SyncStateAll) {
            val user = (_syncState.value as SyncState.SyncStateAll).firebaseUser

        }
    }
}