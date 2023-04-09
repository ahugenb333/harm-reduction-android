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
                    val sorted = it.sortedBy {
                        it.id.idToDateTime()
                    }
                    val allDays = generateAllDays(sorted)
                    insertDays(allDays)
                    val today = allDays.filterToday()[0]
                    _trackerState.value = TrackerState.TrackerStateAll(all = allDays, today = today)
                    completeInit()
                }
        }
    }

    private fun completeInit() {
        val state = _trackerState.value as TrackerState.TrackerStateAll
        val daysOfWeek = getWeekOf(state.selectedDay ?: state.today)
        _trackerState.value =
            state.copy(
                weekBeginnings = getWeekBeginnings().reversed(),
                daysOfWeek = daysOfWeek,
                selectedMonday = daysOfWeek.first {
                    it.id.idToDateTime().dayOfWeek == 1
                }
            )
    }

    fun refreshToday() {
        viewModelScope.launch {
            dayRepository.getDays()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error fetching days", e.toString())
                }
                .collect {
                    val today = it.filterToday()[0]
                    val state = _trackerState.value as TrackerState.TrackerStateAll
                    if (state.selectedDay?.id == today.id) {
                        _trackerState.value = state.copy(
                            all = it,
                            today = today,
                            selectedDay = today
                        )
                    } else {
                        _trackerState.value = state.copy(
                            all = it,
                            today = today,
                        )
                    }
                }
        }
    }

    //Creates a Day object for each day between the beginning and end of days in `days` (inclusive)
    private fun generateAllDays(days: List<Day>): List<Day> {
        if (days.isEmpty()) return listOf(Day())

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
        val newDrinks = currentTrackerState.today.drinks + drinks
        updateDay(currentTrackerState.today.copy(drinks = newDrinks))
        return newDrinks
    }

    fun updateCravingsToday(cravings: Int) {
        val currentTrackerState = _trackerState.value as TrackerState.TrackerStateAll
        updateDay(currentTrackerState.today.copy(cravings = cravings))
    }

    fun addCravingsToday(cravings: Int) {
        val currentTrackerState = _trackerState.value as TrackerState.TrackerStateAll
        val newCravings = currentTrackerState.today.cravings + cravings
        updateDay(currentTrackerState.today.copy(cravings = newCravings))
    }

    fun updateMoneySpentToday(moneySpent: Double) {
        val currentTrackerState = _trackerState.value as TrackerState.TrackerStateAll
        updateDay(currentTrackerState.today.copy(moneySpent = moneySpent))
    }

    fun addMoneySpentToday(moneySpent: Double) {
        val currentTrackerState = _trackerState.value as TrackerState.TrackerStateAll
        val newMoneySpent = currentTrackerState.today.moneySpent + moneySpent
        updateDay(currentTrackerState.today.copy(moneySpent = newMoneySpent))
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

    private fun getWeekBeginnings(): List<Day> {
        val beginnings = mutableListOf<Day>()
        val days = (_trackerState.value as TrackerState.TrackerStateAll).all
        beginnings.addAll(days.filter {
            it.id.idToDateTime().dayOfWeek == 1
        })
        return beginnings
    }

    //returns a list of Days from the beginning to the end of this week.
    private fun getWeekOf(day: Day): List<Day> {
        var dt = day.id.idToDateTime()
        val weekOf = mutableListOf<Day>()

        val days = (_trackerState.value as TrackerState.TrackerStateAll).all

        while (dt.dayOfWeek > 1) {
            dt = dt.minusDays(1)
        }

        val end = dt.plusDays(6)

        while (dt <= end) {
            val nextDay = days.firstOrNull {
                it.id == dt.toId()
            }
            if (nextDay != null) {
                weekOf.add(nextDay)
            } else {
                weekOf.add(Day(dt.toId()))
            }

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
        return getWeekOf(dayLastWeek)
    }

    fun updateSelectedMonday(index: Int) {
        val state = _trackerState.value as TrackerState.TrackerStateAll
        val beginnings = state.weekBeginnings
        if (beginnings.size > index) {
            _trackerState.value =
                state.copy(
                    selectedMonday = beginnings[index],
                    daysOfWeek = getWeekOf(beginnings[index])
                )
        }
    }
}

class TrackerViewModelFactory(private val dayRepository: DayRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrackerViewModel(dayRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}