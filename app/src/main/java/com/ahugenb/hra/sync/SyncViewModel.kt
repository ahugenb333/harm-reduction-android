package com.ahugenb.hra.sync

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncRepository: SyncRepository
    ): ViewModel() {

    private val _firebaseInfo = MutableStateFlow<SyncState>(SyncState.SyncStateEmpty())
    val firebaseInfo: StateFlow<SyncState> = _firebaseInfo

    //Gets firebase info
    fun getFirebaseId() {
        if (_firebaseInfo.value is SyncState.SyncStateAll) return
        viewModelScope.launch {
            syncRepository.getFirebaseInfo()
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.e("Error fetching firebase info", it.toString())
                }
                .collect {
                    _firebaseInfo.value = SyncState.SyncStateAll(it)
                }
        }
    }
}