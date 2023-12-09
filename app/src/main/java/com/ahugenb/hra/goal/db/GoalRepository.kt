package com.ahugenb.hra.goal.db

import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    fun getGoals(): Flow<MutableList<GoalEntity>>

    fun insertGoals(goals: List<GoalEntity>): Flow<Unit>
}