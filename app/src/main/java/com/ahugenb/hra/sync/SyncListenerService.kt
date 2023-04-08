package com.ahugenb.hra.sync

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.wearable.*

class SyncListenerService : WearableListenerService() {

    override fun onDataChanged(buffer: DataEventBuffer) {
        Log.d("onDataChanged", buffer.toString())
        for (event: DataEvent in buffer) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event?.dataItem?.uri?.path
                if (path == "/message_path") {
                    val item = DataMapItem.fromDataItem(event.dataItem)
                    var message = item.dataMap.getString("message") ?: break
                    Log.d("message", message)
                    message = message.substring(5)
                    val intent = Intent()
                    intent.action = ACTION_SEND
                    var moneySpent: Double? = null
                    if (message.startsWith("money:")) {
                        moneySpent = message.substring(6).toDouble()
                    }
                    val payload = SyncWearableData(message, moneySpent)
                    intent.putExtra("payload", payload)

                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                }
            }
        }
        super.onDataChanged(buffer)
    }
}