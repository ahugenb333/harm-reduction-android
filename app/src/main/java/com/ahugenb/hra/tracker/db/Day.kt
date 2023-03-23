package com.ahugenb.hra.tracker.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahugenb.hra.Format
import org.joda.time.DateTime

@Entity(tableName = "days")
data class Day(
    @PrimaryKey val id: String = DateTime.now().toString(Format.DATE_PATTERN_ID),
    @ColumnInfo(name = "drinks") val drinks: Double = 0.0,
    @ColumnInfo(name = "planned") val planned: Double = 0.0,
    @ColumnInfo(name = "money_spent") val moneySpent: Double = 0.0,
    @ColumnInfo(name = "cravings") val cravings: Int = 0,
    @ColumnInfo(name = "notes") val notes: String = ""
)