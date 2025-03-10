package com.ahugenb.hra.tracker.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahugenb.hra.Utils
import org.joda.time.DateTime

@Entity(tableName = "days")
data class Day(
    @PrimaryKey var id: String = DateTime.now().toString(Utils.DATE_PATTERN_ID),
    @ColumnInfo(name = "drinks") var drinks: Double = 0.0,
    @ColumnInfo(name = "planned") var planned: Double = 0.0,
    @ColumnInfo(name = "money_spent") var moneySpent: Double = 0.0,
    @ColumnInfo(name = "cravings") var cravings: Int = 0,
    @ColumnInfo(name = "notes") var notes: String = ""
)