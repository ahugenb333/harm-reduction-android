package com.ahugenb.hra.tracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ahugenb.hra.Format
import com.ahugenb.hra.Format.Companion.idToDateTime
import com.ahugenb.hra.Format.Companion.toId
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
                        it.id.idToDateTime()
                    }

                    insertDays(allDays)

                    _trackerState.value = TrackerState.TrackerStateAll(all = allDays,
                        today = allDays.filterToday()[0])
                }
        }
    }

    //TODO include the monday before the earliest, not just the earliest
    //Creates a Day object for each day between the beginning and end of days in `days` (inclusive)
    private fun generateAllDays(days: List<Day>): List<Day> {
        if (days.isEmpty()) return listOf(Day())

        val newDays = mutableListOf<Day>()
        newDays.addAll(days)

        //First we find the earliest day in Days (the first day the user opened the app)
        var earliest = days[0]

        days.forEach {
            val earliestDt = earliest.id.idToDateTime()
            val dt = it.id.idToDateTime()
            if (dt < earliestDt) {
                earliest = it
            }
        }
        if (days.filterToday().isEmpty()) {
            newDays.add(Day())
        }

        val today = newDays.filterToday()[0]
        val todayDt = today.id.idToDateTime()
        var nextDt = earliest.id.idToDateTime()

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

    fun updateDrinksToday(drinks: Double) {
        when (val currentTrackerState = _trackerState.value) {
            is TrackerState.TrackerStateAll -> {
                updateDay(currentTrackerState.today.copy(drinks = drinks))
            }
            else -> { }
        }
    }

    fun updateDrinks(day: Day, drinks: Double) {
        updateDay(day.copy(drinks = drinks))
    }

    fun updateCravingsToday(cravings: Int) {
        when (val currentTrackerState = _trackerState.value) {
            is TrackerState.TrackerStateAll -> {
                updateDay(currentTrackerState.today.copy(cravings = cravings))
            }
            else -> { }
        }
    }

    fun updateCravings(day: Day, cravings: Int) {
        updateDay(day.copy(cravings = cravings))
    }

    fun updateMoneySpentToday(moneySpent: Double) {
        when (val currentTrackerState = _trackerState.value) {
            is TrackerState.TrackerStateAll -> {
                updateDay(currentTrackerState.today.copy(moneySpent = moneySpent))
            }
            else -> { }
        }
    }

    fun updateMoneySpent(day: Day, moneySpent: Double) {
        updateDay(day.copy(moneySpent = moneySpent))
    }

    private fun updateDay(day: Day) {
        viewModelScope.launch {
            dayRepository.updateDay(day)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error updating day", e.toString())
                }
                .collect {
                    val all = mutableListOf<Day>()
                    //sync tracker state list with the updated day.
                    when (val trackerState = _trackerState.value) {
                        is TrackerState.TrackerStateAll ->
                            trackerState.all.forEach { all.add(it) }
                        else -> {}
                    }
                    val filtered = all.filterDay(day)
                    if (filtered.isNotEmpty()) {
                        all.remove(filtered[0])
                    }
                    all.add(day)
                    _trackerState.value = TrackerState.TrackerStateAll(day, all)
                }
        }
    }

    //returns a list of Days from the beginning to the end of this week.
    fun getThisWeek(): List<Day> {
        val thisWeek = mutableListOf<Day>()
        var dt = Day().id.idToDateTime()

        while (dt.dayOfWeek > 0) {
            dt = dt.minusDays(1)
        }

        val days = when(val trackerState = _trackerState.value) {
            is TrackerState.TrackerStateAll -> trackerState.all
            else -> return listOf()
        }

        val endDt = dt.plusDays(7)
        while (dt < endDt) {
            val filtered = days.filter {
                it.id == dt.toId()
            }
            if (filtered.isNotEmpty()) {
                thisWeek.add(filtered[0])
            } else {
                thisWeek.add(Day(dt.toId()))
            }
            dt = dt.plusDays(1)
        }

        return thisWeek
    }

    private fun todaysId(): String = DateTime.now().toId()

    private fun Day.isToday(): Boolean = this.id == todaysId()

    private fun List<Day>.filterToday(): List<Day> = this.filter { it.isToday() }

    private fun List<Day>.filterDay(day: Day) = this.filter { day.id == it.id }
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