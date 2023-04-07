package com.ahugenb.hra

import kotlinx.coroutines.flow.Flow

interface WearRepository {

    fun sendWholeDrink(): Flow<Unit>

    fun sendHalfDrink(): Flow<Unit>

    fun sendCraving(): Flow<Unit>

    fun sendMoney(money: Double): Flow<Unit>
}