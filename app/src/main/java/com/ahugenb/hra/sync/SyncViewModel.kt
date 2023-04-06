package com.ahugenb.hra.sync

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SyncViewModel(
    private val syncRepository: SyncRepository
    ): ViewModel() {

//    private val _trackerState: MutableStateFlow<TrackerState> =
//        MutableStateFlow(TrackerState.TrackerStateEmpty())
//    val trackerState: StateFlow<TrackerState> = _trackerState


    private val _firebaseInfo = MutableStateFlow<SyncState>(SyncState.SyncStateEmpty())
    val firebaseInfo: StateFlow<SyncState> = _firebaseInfo

    fun getFirebaseInfo() {
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

class SyncViewModelFactory(private val syncRepository: SyncRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SyncViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SyncViewModel(syncRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}