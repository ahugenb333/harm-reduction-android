package com.ahugenb.hra.goal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ahugenb.hra.goal.db.Goal
import com.ahugenb.hra.goal.db.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class GoalViewModel(
    private val goalRepository: GoalRepository
): ViewModel() {

    private val _goalState: MutableStateFlow<GoalState> = MutableStateFlow(GoalState())
    val goalState: StateFlow<GoalState> = _goalState

    init {
        viewModelScope.launch {
            goalRepository.getGoals()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error fetching goals", e.toString())
                }
                .collect {
                    it.add(Goal(0))
                    it.add(Goal(1))
                    _goalState.value = _goalState.value.copy(goals = it.toMutableList())
                }
        }
    }

    fun saveGoal(goal: Goal, index: Int) {
        val goals = _goalState.value.goals
        if (index >= goals.size) {
            goals.add(goal)
        } else {
            goals[index] = goal
        }
        _goalState.value.goals.clear()
        _goalState.value.goals.addAll(goals)

        viewModelScope.launch {
            goalRepository.insertGoals(goals)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error saving goals", e.toString())
                }
                .collect {
                    Log.d("GoalViewModel", "Saved goals")
                    _goalState.value = _goalState.value.copy(hasSelectedGoal = false)
                }
        }
    }
}

class GoalViewModelFactory(
    private val goalRepository: GoalRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoalViewModel(goalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

