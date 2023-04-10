package com.ahugenb.hra.sync

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ahugenb.hra.HraApplication
import com.ahugenb.hra.Utils.Companion.filterToday
import com.ahugenb.hra.tracker.db.DayRepository
import com.google.android.gms.wearable.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.joda.time.DateTime
import java.util.concurrent.Flow

class SyncListenerService : WearableListenerService() {
    companion object {
        const val MESSAGE_PATH = "/message_path"
        const val MESSAGE = "message"
        const val MESSAGE_HALF_DRINK = "half_drink"
        const val MESSAGE_DRINK = "drink"
        const val MESSAGE_CRAVING = "craving"
        const val MESSAGE_MONEY = "money"
        const val DATA = "data"
    }

    override fun onDataChanged(buffer: DataEventBuffer) {
        val repository = if (application != null) {
            (application as HraApplication).dayRepository
        } else {
            null
        }
        repository?.let {
            Log.d("onDataChanged", buffer.toString())
            for (event: DataEvent in buffer) {
                if (event.type == DataEvent.TYPE_CHANGED) {
                    val path = event?.dataItem?.uri?.path
                    if (path == MESSAGE_PATH) {
                        val item = DataMapItem.fromDataItem(event.dataItem)
                        var message = item.dataMap.getString(MESSAGE) ?: break
                        Log.d(MESSAGE, message)
                        message = message.substring(5)  //remove timestamp

                        CoroutineScope(Dispatchers.IO).launch {
                            updateDatabase(repository, message)
                        }
                    }
                }
            }
        }
        super.onDataChanged(buffer)
    }

    private suspend fun updateDatabase(repository: DayRepository, message: String) {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            repository.getDays()
                .flowOn(Dispatchers.Default)
                .catch {
                    Log.e("updateDatabase - getDays", it.message ?: "Error")
                }
                .collect {
                    Log.d("updateDatabase - getDays", "days retrieved")
                    val filtered = it.filterToday()
                    var data: String? = null
                    if (filtered.isNotEmpty()) {
                        var today = filtered[0]
                        when (message) {
                            MESSAGE_HALF_DRINK -> {
                                today = today.copy(drinks = today.drinks + 0.5)
                                data = String.format("%.2f Drinks (%.2f Planned)",
                                    today.drinks, today.planned)
                            }
                            MESSAGE_DRINK -> {
                                today = today.copy(drinks = today.drinks + 1.0)
                                data = String.format("%.2f Drinks (%.2f Planned)",
                                    today.drinks, today.planned)
                            }
                            MESSAGE_CRAVING -> {
                                today = today.copy(cravings = today.cravings + 1)
                                data = "${today.cravings} Cravings"
                            }
                            MESSAGE_MONEY -> {
                                today = today.copy(moneySpent = today.moneySpent + 10)
                                data = String.format("$%.2f Spent", today.moneySpent)
                            }
                        }
                        repository.updateDay(today)
                            .flowOn(Dispatchers.Default)
                            .catch {
                            Log.e("updateDatabase - updateDay", it.message ?: "Error")
                        }.collect {
                                Log.d("updateDatabase - updateDay", "Updated")
                                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent(ACTION_SEND))
                                data?.let { d -> sendData(d) }
                        }
                    }
                }
        }
    }

    private fun sendData(data: String) {
        val map = PutDataMapRequest.create(MESSAGE_PATH)
        val newMessage = DateTime.now().millis.toString().reversed().substring(0,5) + data
        map.dataMap.putString(DATA, newMessage)
        val request = map.asPutDataRequest()
        request.setUrgent()

        val dataItemTask = Wearable.getDataClient(applicationContext).putDataItem(request)
        dataItemTask.addOnSuccessListener {
            Log.d("Data sent", "Message: $newMessage")
        }.addOnFailureListener {
            Log.e("Data not sent", "Message: $newMessage")
        }
    }


}