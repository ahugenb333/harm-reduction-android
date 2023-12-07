package com.ahugenb.hra.goal.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey val goalNumber: Int,
    @ColumnInfo(name = "red") val red: Double = 0.0,
    @ColumnInfo(name = "yellow") val yellow: Double = 0.0,
    @ColumnInfo(name = "green") val green: Double = 0.0,
    @ColumnInfo(name = "period") val period: GoalPeriod = GoalPeriod.DAILY,
    @ColumnInfo(name = "type") val type: GoalType = GoalType.RedGreen,
)

enum class GoalPeriod {
    DAILY,
    WEEKLY
}

enum class GoalStatus {
    RED,
    YELLOW,
    GREEN
}

enum class GoalType {
    RedGreen,
    RedYellowGreen
}