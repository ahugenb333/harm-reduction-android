package com.ahugenb.hra.goal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ahugenb.hra.goal.db.Goal
import com.ahugenb.hra.goal.db.GoalImpl
import com.ahugenb.hra.goal.db.GoalPeriod
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

    private val _goalList: MutableStateFlow<MutableList<Goal>> = MutableStateFlow(mutableListOf())
    val goalList: StateFlow<MutableList<Goal>> = _goalList

    init {
        viewModelScope.launch {
            goalRepository.getGoals()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error fetching goals", e.toString())
                }
                .collect {
                    it.add(GoalImpl(
                        red = 3.0,
                        yellow = 2.0,
                        green = 1.0,
                        period = GoalPeriod.DAILY,
                    ))
                    it.add(GoalImpl(
                        red = 3.0,
                        yellow = 2.0,
                        green = 1.0,
                        period = GoalPeriod.WEEKLY,
                    ))
                    _goalList.value = it
                }
        }
    }

    fun saveGoal(goal: Goal, index: Int) {
        val goals = _goalList.value
        if (index >= goals.size) {
            goals.add(goal)
        } else {
            goals[index] = goal
        }
        viewModelScope.launch {
            goalRepository.insertGoals(goals)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e("Error saving goals", e.toString())
                }
                .collect {
                    Log.d("GoalViewModel", "Saved goals")
                    _goalList.value = goals
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

