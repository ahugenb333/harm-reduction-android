package com.ahugenb.hra.sync

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ahugenb.hra.Utils.Companion.smartToDouble
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class SyncListenerService : WearableListenerService() {
    companion object {
        private const val MESSAGE_PATH = "/message_path"
        private const val PHONE_PATH = "/phone"
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("onMessageReceived", messageEvent.toString())

        if (messageEvent.path == MESSAGE_PATH) {
            val intent = Intent()
            val sourceNodeId = messageEvent.sourceNodeId
            var message = String(messageEvent.data)
            var moneySpent: Double? = null
            if (message.startsWith("money:")) {
                moneySpent = message.substring(6).smartToDouble()
                message = "money"
            }
            intent.action = ACTION_SEND
            val payload = SyncWearableData(message, moneySpent, sourceNodeId)
            intent.putExtra("payload", payload)

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }

        super.onMessageReceived(messageEvent)
    }
}