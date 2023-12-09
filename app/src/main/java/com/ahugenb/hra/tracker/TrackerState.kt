package com.ahugenb.hra.tracker

import com.ahugenb.hra.goal.db.Goal
import com.ahugenb.hra.goal.db.GoalPeriod
import com.ahugenb.hra.goal.db.GoalUnit
import com.ahugenb.hra.tracker.db.Day

sealed class TrackerState {
    class TrackerStateEmpty: TrackerState()
    data class TrackerStateAll(
        val today: Day,
        val selectedDay: Day? = today,
        val selectedMonday: Day = today,
        val all: List<Day> = listOf(),
        val daysOfWeek: List<Day> = listOf(),
        val weekBeginnings: List<Day> = listOf()
    ): TrackerState()
}

fun TrackerState.TrackerStateAll.getActualValue(goal: Goal): Double {
    return when (goal.period) {
        GoalPeriod.DAILY -> when(goal.unit) {
            GoalUnit.DRINKS -> today.drinks
            GoalUnit.CRAVINGS -> today.cravings.toDouble()
            GoalUnit.MONEY -> today.moneySpent
        }
        GoalPeriod.WEEKLY -> when(goal.unit) {
            GoalUnit.DRINKS -> daysOfWeek.sumOf { it.drinks }
            GoalUnit.CRAVINGS -> daysOfWeek.sumOf { it.cravings.toDouble() }
            GoalUnit.MONEY -> daysOfWeek.sumOf { it.moneySpent }
        }
        else -> 0.0 //todo
    }
}
