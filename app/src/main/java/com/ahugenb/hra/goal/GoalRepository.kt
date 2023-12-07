package com.ahugenb.hra.goal

import com.ahugenb.hra.goal.db.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    fun getGoals(): Flow<List<Goal>>

    fun insertGoal(goal: Goal): Flow<Unit>

    fun updateGoals(goal: Goal): Flow<Unit>
}