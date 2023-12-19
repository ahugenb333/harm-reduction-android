package com.ahugenb.hra.goal.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//Performs mapping to/from GoalImpl for database
class GoalRepositoryImpl(
    private val goalDatabase: GoalDatabase
): GoalRepository {

    override fun getGoals(): Flow<MutableList<Goal>> = flow {
        emit(goalDatabase.goalDao().getGoals().toMutableList())
    }

    override fun insertGoals(goals: List<Goal>): Flow<Unit> = flow {
        goalDatabase.goalDao().insertGoals(goals.map { it as GoalImpl })
        emit(Unit)
    }
}