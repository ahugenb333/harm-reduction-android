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
    val consecutiveSuccesses: Int
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
    @ColumnInfo(name = "consecutive_successes") override val consecutiveSuccesses: Int = 0
): Goal {
    override fun isSavable(): Boolean =
        if (this.isYellowType()) {
            this.red > this.yellow && this.yellow > this.green
        } else {
            this.red > this.green
        }
    override fun getGoalStatus(actualValue: Double): GoalStatus {
        if (actualValue >= this.red) {
            return GoalStatus.RED
        } else if (isYellowType() && actualValue < this.red && actualValue >= this.yellow) {
            return GoalStatus.YELLOW
        }
        return GoalStatus.GREEN
    }

    override fun isYellowType(): Boolean {
        return this.yellow > 0
    }
}

enum class GoalPeriod(val periodName: String? = null) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    BIWEEKLY("Biweekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly")
}

enum class GoalUnit(val unitName: String? = null) {
    DRINKS("Drinks"),
    MONEY("Money"),
    CRAVINGS("Cravings")
}

enum class GoalStatus {
    GREEN,
    YELLOW,
    RED
}