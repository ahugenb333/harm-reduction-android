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
    @ColumnInfo(name = "unit") val unit: GoalUnit = GoalUnit.DRINKS,
    @ColumnInfo(name = "type") val type: GoalType = GoalType.RedGreen,
    @ColumnInfo(name = "status") val status: GoalStatus = GoalStatus.PASSING,
)

enum class GoalPeriod {
    DAILY,
    WEEKLY,
    BIWEEKLY,
    MONTHLY,
    QUARTERLY,
    YEARLY
}

enum class GoalUnit {
    DRINKS,
    MONEY,
    CRAVINGS
}

enum class GoalStatus {
    PASSING,
    FAILING
}

enum class GoalType {
    RedGreen,
    RedYellowGreen
}