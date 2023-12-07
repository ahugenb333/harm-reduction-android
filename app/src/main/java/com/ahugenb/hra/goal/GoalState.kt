package com.ahugenb.hra.goal

import com.ahugenb.hra.goal.db.Goal
import com.ahugenb.hra.goal.db.GoalType

data class GoalState(
    val goals: MutableList<Goal> = mutableListOf(),
    var selectedGoal: Goal? = null
)

fun GoalState.isSavable() = selectedGoal != null &&
        when(selectedGoal!!.type) {
            GoalType.RedGreen -> {
                selectedGoal!!.green < selectedGoal!!.red
            }
            GoalType.RedYellowGreen -> {
                selectedGoal!!.green < selectedGoal!!.yellow &&
                        selectedGoal!!.yellow < selectedGoal!!.red
            }
        }

