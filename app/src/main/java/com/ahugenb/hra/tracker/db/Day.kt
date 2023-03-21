package com.ahugenb.hra.tracker.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days")
data class Day(
    @PrimaryKey val id: String, //DateTime.Day
    @ColumnInfo(name = "drinks") val drinks: Double,
    @ColumnInfo(name = "planned") val planned: Double,
    @ColumnInfo(name = "money_spent") val moneySpent: Double,
    @ColumnInfo(name = "cravings") val cravings: Int,
    @ColumnInfo(name = "notes") val notes: String
)