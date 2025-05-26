package com.ahugenb.hra.tracker.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ahugenb.hra.Utils
import org.joda.time.DateTime

@Entity(tableName = "days")
data class Day(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "drinks") var drinks: Double,
    @ColumnInfo(name = "planned") var planned: Double,
    @ColumnInfo(name = "money_spent") var moneySpent: Double,
    @ColumnInfo(name = "cravings") var cravings: Int,
    @ColumnInfo(name = "notes") var notes: String
) {
    @Ignore
    constructor() : this(
        id = DateTime.now().toString(Utils.DATE_PATTERN_ID),
        drinks = 0.0,
        planned = 0.0,
        moneySpent = 0.0,
        cravings = 0,
        notes = ""
    )
}