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
import org.joda.time.format.DateTimeFormat

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
                .collect { it ->
                    val allDays = generateAllDays(it).sortedBy {
                        it.id.toDateTime()
                    }

                    insertDays(allDays)

                    _trackerState.value = TrackerState.TrackerStateAll(all = allDays,
                        today = allDays.filterToday()[0])
                }
        }
    }

    //Creates a Day object for each day between the beginning and end of days in `days` (inclusive)
    private fun generateAllDays(days: List<Day>): List<Day> {
        if (days.isEmpty()) return listOf(Day())

        val newDays = mutableListOf<Day>()
        newDays.addAll(days)

        //First we find the earliest day in Days (the first day the user opened the app)
        var earliest = days[0]

        days.forEach {
            val earliestDt = earliest.id.toDateTime()
            val dt = it.id.toDateTime()
            if (dt < earliestDt) {
                earliest = it
            }
        }
        if (days.filterToday().isEmpty()) {
            newDays.add(Day())
        }

        val today = newDays.filterToday()[0]
        val todayDt = today.id.toDateTime()
        var nextDt = earliest.id.toDateTime()

        //Next we iterate from earliest -> today, adding a Day object where one does not exist.
        while (nextDt < todayDt) {
            val isNotInDays = newDays.none {
                it.id == nextDt.toId()
            }
            if (isNotInDays) {
                newDays.add(Day(nextDt.toId()))
            }
            nextDt = nextDt.plusDays(1)
        }
        return newDays
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
            is TrackerState.TrackerStateAll -> {
                updateDay(currentTrackerState.today.copy(drinks = drinks))
            }
            else -> { }
        }
    }

    fun updateCravings(cravings: Int) {
        when (val currentTrackerState = _trackerState.value) {
            is TrackerState.TrackerStateAll -> {
                updateDay(currentTrackerState.today.copy(cravings = cravings))
            }
            else -> { }
        }
    }

    fun updateMoneySpent(moneySpent: Double) {
        when (val currentTrackerState = _trackerState.value) {
            is TrackerState.TrackerStateAll -> {
                updateDay(currentTrackerState.today.copy(moneySpent = moneySpent))
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
                    _trackerState.value = TrackerState.TrackerStateAll(day)
                }
        }
    }

    private fun String.toDateTime(): DateTime =
        DateTime.parse(this, DateTimeFormat.forPattern(Format.DATE_PATTERN))

    private fun DateTime.toId(): String = this.toString(Format.DATE_PATTERN)

    private fun todaysId(): String = DateTime.now().toId()

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