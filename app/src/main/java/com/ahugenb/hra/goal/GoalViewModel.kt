package com.ahugenb.hra.goal

import androidx.lifecycle.ViewModel
import com.ahugenb.hra.goal.db.Goal
import kotlinx.coroutines.flow.MutableStateFlow

class GoalViewModel(
    private val goalRepository: GoalRepository
): ViewModel() {

    private val _goalState: MutableStateFlow<List<Goal>> = MutableStateFlow(listOf())
}