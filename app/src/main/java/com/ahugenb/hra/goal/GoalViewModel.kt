package com.ahugenb.hra.goal

import androidx.lifecycle.ViewModel
import com.ahugenb.hra.goal.db.Goal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GoalViewModel(
    private val goalRepository: GoalRepository
): ViewModel() {

    private val _goalState: MutableStateFlow<GoalState> = MutableStateFlow(GoalState())
    val goalState: StateFlow<GoalState> = _goalState

    fun addEmptyGoal() {
        _goalState.value.goals.add(Goal())
    }
}


