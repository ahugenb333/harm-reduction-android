package com.ahugenb.hra

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.joda.time.DateTime
import java.util.concurrent.ExecutionException

class WearRepositoryImpl(
    private val context: Context
): WearRepository {
    companion object {
        const val MESSAGE_HALF_DRINK = "half_drink"
        const val MESSAGE_DRINK = "drink"
        const val MESSAGE_CRAVING = "craving"
        const val MESSAGE_MONEY = "money"
        const val MESSAGE_PATH = "/message_path"
        const val MESSAGE = "message"
    }

    override fun sendWholeDrink(): Flow<Unit> = flow {
        sendData(MESSAGE_DRINK)
        emit(Unit)
    }

    override fun sendHalfDrink(): Flow<Unit> = flow {
        sendData(MESSAGE_HALF_DRINK)
        emit(Unit)
    }

    override fun sendCraving(): Flow<Unit> = flow {
        sendData(MESSAGE_CRAVING)
        emit(Unit)
    }

    override fun sendMoney(): Flow<Unit> = flow {
        sendData(MESSAGE_MONEY)
        emit(Unit)
    }

    private fun sendData(message: String) {
        val map = PutDataMapRequest.create(MESSAGE_PATH)
        val newMessage = DateTime.now().millis.toString().reversed().substring(0,5) + message
        map.dataMap.putString(MESSAGE, newMessage)
        val request = map.asPutDataRequest()
        request.setUrgent()

        val dataItemTask = Wearable.getDataClient(context).putDataItem(request)
        dataItemTask.addOnSuccessListener {
            Log.d("Data sent", "Message: $newMessage")
        }.addOnFailureListener {
            Log.e("Data not sent", "Message: $newMessage")
        }
    }
}