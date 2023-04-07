package com.ahugenb.hra

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import java.util.concurrent.ExecutionException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WearRepositoryImpl(
    private val context: Context
): WearRepository {
    companion object {
        const val MESSAGE_HALF_DRINK = "half_drink"
        const val MESSAGE_DRINK = "drink"
        const val MESSAGE_CRAVING = "craving"
        const val MESSAGE_MONEY = "money"
        const val MESSAGE_PATH = "/message_path"
    }

    override fun sendWholeDrink(): Flow<Unit> = flow {
        sendMessage(MESSAGE_DRINK.toByteArray())
        emit(Unit)
    }

    override fun sendHalfDrink(): Flow<Unit> = flow {
        sendMessage(MESSAGE_HALF_DRINK.toByteArray())
        emit(Unit)
    }

    override fun sendCraving(): Flow<Unit> = flow {
        sendMessage(MESSAGE_CRAVING.toByteArray())
        emit(Unit)
    }

    override fun sendMoney(money: Double): Flow<Unit> = flow {
        sendMessage(MESSAGE_MONEY.plus(":").plus(String.format("%.2f", money)).toByteArray())
        emit(Unit)
    }

    private fun sendMessage(message: ByteArray) {
        try {
            val nodes = Tasks.await(getNodes())

            for (node in nodes) {
                Wearable.getMessageClient(context)
                    .sendMessage(node.id, MESSAGE_PATH, message)
            }
        } catch (e: ExecutionException) {
            Log.e("Error sending message: $message", e.stackTraceToString())
        }
    }

    private fun getNodes(): Task<List<Node>> {
        return Wearable.getNodeClient(context.applicationContext).connectedNodes
    }
}