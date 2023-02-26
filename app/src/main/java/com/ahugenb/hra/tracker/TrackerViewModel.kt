package com.ahugenb.hra.tracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ahugenb.hra.tracker.db.DatabaseHelper
import com.ahugenb.hra.tracker.db.Day
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TrackerViewModel(
    private val dbHelper: DatabaseHelper
) : ViewModel() {
    companion object {
        const val EMPTY_DAY = "empty_day"
        const val DATE_PATTERN = "dd/MM/yyyy"
    }

    private val _trackerState: MutableStateFlow<TrackerState> =
        MutableStateFlow(TrackerState.TrackerStateEmpty())
    val trackerState: StateFlow<TrackerState> = _trackerState

    init {
        fetchDays()
    }

    private fun fetchDays() {
        viewModelScope.launch {
            dbHelper.getDays()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error fetching days", e.toString())
                }
                .collect {
                    _trackerState.value = TrackerState.TrackerStateDays(it)
                }
        }
    }

    fun updateDays(days: List<Day>) {
        viewModelScope.launch {
            dbHelper.insertAll(days)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error updating days", e.toString())
                }
                .collect {
                    _trackerState.value = TrackerState.TrackerStateDays(it)
                }
        }
    }
}

class TrackerViewModelFactory(private val dbHelper: DatabaseHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrackerViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}