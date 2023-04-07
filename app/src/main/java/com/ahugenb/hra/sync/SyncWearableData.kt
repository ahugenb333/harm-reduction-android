package com.ahugenb.hra.sync

import java.io.Serializable

data class SyncWearableData(
    val message: String,
    val moneySpent: Double?,
    val sourceNodeId: String
): Serializable
