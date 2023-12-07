package com.ahugenb.hra.goal

import com.ahugenb.hra.goal.db.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    fun getGoals(): Flow<List<Goal>>

    fun insertGoals(goals: List<Goal>): Flow<Unit>
}