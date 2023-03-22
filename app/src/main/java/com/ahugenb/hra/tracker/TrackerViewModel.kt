package com.ahugenb.hra.tracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ahugenb.hra.Format
import com.ahugenb.hra.tracker.db.DayRepository
import com.ahugenb.hra.tracker.db.Day
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class TrackerViewModel(
    private val dayRepository: DayRepository
) : ViewModel() {

    private val _trackerState: MutableStateFlow<TrackerState> =
        MutableStateFlow(TrackerState.TrackerStateEmpty())
    val trackerState: StateFlow<TrackerState> = _trackerState

    init {
        fetchDays()
    }

    private fun fetchDays() {
        viewModelScope.launch {
            dayRepository.getDays()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error fetching days", e.toString())
                }
                .collect {
                    if (it.filterToday().isEmpty()) {
                        val today = Day()

                        insertDays(listOf(today))
                        _trackerState.value = TrackerState.TrackerStateDay(today)
                    } else {
                        _trackerState.value = TrackerState.TrackerStateDay(it.filterToday()[0])
                    }
                }
        }
    }

    private fun insertDays(days: List<Day>) {
        viewModelScope.launch {
            dayRepository.insertDays(days)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error inserting days", e.toString())
                }
                .collect {
                    Log.d("Inserted days successfully", "")
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

    fun updateMoneySpent(moneySpent: Double) {
        when (val currentTrackerState = _trackerState.value) {
            is TrackerState.TrackerStateDay -> {
                updateDay(currentTrackerState.day.copy(moneySpent = moneySpent))
            }
            else -> { }
        }
    }

    private fun updateDay(day: Day) {
        viewModelScope.launch {
            dayRepository.updateDay(day)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error updating day", e.toString())
                }
                .collect {
                    _trackerState.value = TrackerState.TrackerStateDay(day)
                }
        }
    }

    private fun todaysId(): String = DateTime.now().toString(Format.DATE_PATTERN)

    private fun Day.isToday(): Boolean = this.id == todaysId()

    private fun List<Day>.filterToday(): List<Day> = this.filter { it.isToday() }
}

class TrackerViewModelFactory(private val dbHelper: DayRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrackerViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}