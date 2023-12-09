package com.ahugenb.hra.goal.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GoalRepositoryImpl(
    private val goalDatabase: GoalDatabase
): GoalRepository {

    override fun getGoals(): Flow<MutableList<GoalEntity>> = flow {
        emit(goalDatabase.goalDao().getGoals().toMutableList())
    }

    override fun insertGoals(goals: List<GoalEntity>): Flow<Unit> = flow {
        goalDatabase.goalDao().insertGoals(goals)
        emit(Unit)
    }
}