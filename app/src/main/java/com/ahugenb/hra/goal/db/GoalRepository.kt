package com.ahugenb.hra.goal.db

import com.ahugenb.hra.goal.db.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    fun getGoals(): Flow<MutableList<Goal>>

    fun insertGoals(goals: List<Goal>): Flow<Unit>
}