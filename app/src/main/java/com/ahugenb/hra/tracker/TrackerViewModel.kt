package com.ahugenb.hra.tracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ahugenb.hra.Utils.Companion.filterToday
import com.ahugenb.hra.Utils.Companion.idToDateTime
import com.ahugenb.hra.Utils.Companion.isToday
import com.ahugenb.hra.Utils.Companion.toId
import com.ahugenb.hra.tracker.db.Day
import com.ahugenb.hra.tracker.db.DayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TrackerViewModel(
    private val dayRepository: DayRepository
) : ViewModel() {

    private val _trackerState: MutableStateFlow<TrackerState> =
        MutableStateFlow(TrackerState.TrackerStateEmpty())
    val trackerState: StateFlow<TrackerState> = _trackerState

    init {
        initDays()
    }

    //fetches Day objects, filling in any gaps with empty Days
    private fun initDays() {
        viewModelScope.launch {
            dayRepository.getDays()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error fetching days", e.toString())
                }
                .collect { it ->
                    var days = it.sortedBy {
                        it.id.idToDateTime()
                    }
                    if (days.isEmpty()) {
                        days = generateWeekOf(Day())
                    }
                    val allDays = generateAllDays(days)
                    insertDays(allDays)
                    val today = allDays.filterToday()[0]
                    val daysOfWeek = getWeekOf(allDays, today)

                    _trackerState.value = TrackerState.TrackerStateAll(
                        all = allDays,
                        today = today,
                        weekBeginnings = getWeekBeginnings(allDays).reversed(),
                        daysOfWeek = daysOfWeek,
                        selectedMonday = daysOfWeek.first {
                            it.id.idToDateTime().dayOfWeek == 1
                        }
                    )
                }
        }
    }

    //Creates a Day object for each day between the beginning and end of days in `days` (inclusive)
    private fun generateAllDays(days: List<Day>): List<Day> {
        val allDays = mutableListOf<Day>()
        allDays.addAll(days)

        if (days.filterToday().isEmpty()) {
            allDays.add(Day())
        }

        val today = allDays.filterToday()[0]
        val earliest = days[0]

        //We rewind to the first day of `earliest`'s week
        var nextDt = earliest.id.idToDateTime()
        while (nextDt.dayOfWeek > 1) {
            nextDt = nextDt.minusDays(1)
        }

        //We fast forward from `today` to the last day of `today`'s week
        var finalDt = today.id.idToDateTime()
        while (finalDt.dayOfWeek < 7) {
            finalDt = finalDt.plusDays(1)
        }

        //Next we iterate from monday of earliest to sunday of final, adding a Day object where one does not exist.
        while (nextDt <= finalDt) {
            val isNotInDays = allDays.none {
                it.id == nextDt.toId()
            }
            if (isNotInDays) {
                allDays.add(Day(nextDt.toId()))
            }
            nextDt = nextDt.plusDays(1)
        }
        return allDays
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
        val currentTrackerState = _trackerState.value as TrackerState.TrackerStateAll
        updateDay(currentTrackerState.today.copy(drinks = drinks))
    }

    fun addDrinksToday(drinks: Double): Double {
        val currentTrackerState = _trackerState.value as TrackerState.TrackerStateAll
        updateDay(currentTrackerState.today.copy(drinks = currentTrackerState.today.drinks + drinks))
        return currentTrackerState.today.drinks + drinks
    }

    fun updateCravingsToday(cravings: Int) {
        val currentTrackerState = _trackerState.value as TrackerState.TrackerStateAll
        updateDay(currentTrackerState.today.copy(cravings = cravings))
    }

    fun updateMoneySpentToday(moneySpent: Double) {
        val currentTrackerState = _trackerState.value as TrackerState.TrackerStateAll
        updateDay(currentTrackerState.today.copy(moneySpent = moneySpent))
    }

    fun updateDay(day: Day) {
        viewModelScope.launch {
            dayRepository.updateDay(day)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error updating day", e.toString())
                }
                .collect {
                    val all = mutableListOf<Day>()
                    val daysOfWeek = mutableListOf<Day>()
                    //sync tracker state list with the updated day.
                    val state = _trackerState.value as TrackerState.TrackerStateAll

                    state.all.forEach {
                        if (it.id == day.id) {
                            all.add(day)
                        } else {
                            all.add(it)
                        }
                    }

                    state.daysOfWeek.forEach {
                        if (it.id == day.id) {
                            daysOfWeek.add(day)
                        } else {
                            daysOfWeek.add(it)
                        }
                    }

                    _trackerState.value = if (day.isToday()) {
                        state.copy(
                            selectedDay = day,
                            today = day,
                            all = all,
                            daysOfWeek = daysOfWeek
                        )
                    } else {
                        state.copy(
                            selectedDay = day,
                            all = all,
                            daysOfWeek = daysOfWeek
                        )
                    }
                }
        }
    }

    private fun getWeekBeginnings(allDays: List<Day>): List<Day> {
        val beginnings = mutableListOf<Day>()
        beginnings.addAll(allDays.filter {
            it.id.idToDateTime().dayOfWeek == 1
        })
        return beginnings
    }

    private fun generateWeekOf(day: Day): List<Day> {
        var dt = day.id.idToDateTime()
        val weekOf = mutableListOf<Day>()

        while (dt.dayOfWeek > 1) {
            dt = dt.minusDays(1)
        }

        val end = dt.plusDays(6)

        while (dt <= end) {
            weekOf.add(Day(dt.toId()))
            dt = dt.plusDays(1)
        }

        return weekOf
    }

    private fun getWeekOf(allDays: List<Day>, day: Day): List<Day> {
        var dt = day.id.idToDateTime()
        val weekOf = mutableListOf<Day>()

        while (dt.dayOfWeek > 1) {
            dt = dt.minusDays(1)
        }

        val end = dt.plusDays(6)

        while (dt <= end) {
            val nextDay = allDays.firstOrNull {
                it.id == dt.toId()
            } ?: Day(dt.toId())
            weekOf.add(nextDay)
            dt = dt.plusDays(1)
        }

        return weekOf
    }

    fun setSelectedDay(selectedDay: Day?) {
        val state = _trackerState.value as TrackerState.TrackerStateAll

        _trackerState.value =
            state.copy(
                selectedDay = selectedDay
            )
    }

    fun getLastWeek(): List<Day> {
        val state = _trackerState.value as TrackerState.TrackerStateAll
        val dt = state.daysOfWeek[0].id.idToDateTime()
        val rewind = dt.minusDays(7)
        val dayLastWeek = state.all.firstOrNull {
            it.id == rewind.toId()
        } ?: Day(rewind.toId())
        return getWeekOf(state.all, dayLastWeek)
    }

    fun updateSelectedMonday(index: Int) {
        val state = _trackerState.value as TrackerState.TrackerStateAll
        val beginnings = state.weekBeginnings
        if (beginnings.size > index) {
            _trackerState.value =
                state.copy(
                    selectedMonday = beginnings[index],
                    daysOfWeek = getWeekOf(state.all, beginnings[index])
                )
        }
    }
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