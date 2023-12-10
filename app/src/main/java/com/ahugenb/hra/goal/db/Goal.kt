package com.ahugenb.hra.goal.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

interface Goal {
    val goalId: UUID
    val red: Double
    val yellow: Double
    val green: Double
    val period: GoalPeriod
    val unit: GoalUnit
    fun getGoalStatus(actualValue: Double): GoalStatus
    fun isSavable(): Boolean

    fun isYellowType(): Boolean
}

@Entity(tableName = "goals")
data class GoalImpl(
    @PrimaryKey override val goalId : UUID = UUID.randomUUID(),
    @ColumnInfo(name = "red") override val red: Double = 0.0,
    @ColumnInfo(name = "yellow") override val yellow: Double = 0.0,
    @ColumnInfo(name = "green") override val green: Double = 0.0,
    @ColumnInfo(name = "period") override val period: GoalPeriod = GoalPeriod.DAILY,
    @ColumnInfo(name = "unit") override val unit: GoalUnit = GoalUnit.DRINKS,
): Goal {
    override fun isSavable(): Boolean =
        if (this.isYellowType()) {
            this.red > this.yellow && this.yellow > this.green
        } else {
            this.red > this.green
        }
    override fun getGoalStatus(actualValue: Double): GoalStatus {
        if (!this.isYellowType()) {
            if (actualValue >= this.red) return GoalStatus.RED
        } else if (actualValue < this.red && actualValue >= this.yellow) {
            return GoalStatus.YELLOW
        }
        return GoalStatus.GREEN
    }

    override fun isYellowType(): Boolean {
        return this.yellow > 0
    }
}

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
    GREEN,
    YELLOW,
    RED
}