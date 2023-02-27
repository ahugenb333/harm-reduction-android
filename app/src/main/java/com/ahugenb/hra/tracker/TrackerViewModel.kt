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
import org.joda.time.DateTime

class TrackerViewModel(
    private val dbHelper: DatabaseHelper
) : ViewModel() {
    companion object {
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
                    if (it.filterToday().isEmpty()) {
                        val today = Day(todaysId(), 0.0, 0.0, 0.0, 0, "")

                        insertDays(listOf(
                           today
                        ))
                        _trackerState.value = TrackerState.TrackerStateDay(today)
                    }
                }
        }
    }

    private fun insertDays(days: List<Day>) {
        viewModelScope.launch {
            dbHelper.insertAll(days)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error inserting days", e.toString())
                }
        }
    }

    fun updateDrinks(drinks: Double) {
        when (val currentTrackerState = _trackerState.value) {
            is TrackerState.TrackerStateDay -> {
                updateDay(currentTrackerState.day.copy(drinks = drinks))
            }
            else -> { }
        }
    }

    fun updateCravings(cravings: Int) {
        when (val currentTrackerState = _trackerState.value) {
            is TrackerState.TrackerStateDay -> {
                updateDay(currentTrackerState.day.copy(cravings = cravings))
            }
            else -> { }
        }
    }

    private fun updateDay(day: Day) {
        viewModelScope.launch {
            dbHelper.updateDay(day)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error updating day", e.toString())
                }
                .collect {
                    _trackerState.value = TrackerState.TrackerStateDay(day)
                }
        }
    }

    private fun todaysId(): String = DateTime.now().toString(DATE_PATTERN)

    private fun Day.isToday(): Boolean = this.id == todaysId()

    private fun List<Day>.filterToday(): List<Day> = this.filter { it.isToday() }
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