package com.davi.messagewearable.services

import android.content.Intent
import android.util.Log
import com.davi.messagewearable.MainActivity
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException


class DataLayerListenerService : WearableListenerService() {

    private val messageClient by lazy { Wearable.getMessageClient(this) }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)

        dataEvents.map { dataEvent ->
            Log.d(TAG, "DT DC ${dataEvent.dataItem.uri.toString()}")
        }

        dataEvents.forEach { dataEvent ->
            val uri = dataEvent.dataItem.uri
            when (uri.path) {
                MOBILE_PATH -> {
                    scope.launch {
                        try {
                            val nodeId = uri.host!!
                            val payload = uri.toString().toByteArray()

                            DataMapItem.fromDataItem(dataEvent.dataItem).dataMap.apply {
                                Log.d(TAG, "Message Mobile ${nodeId}" + getString(MOBILE_KEY))
                            }

//                            messageClient.sendMessage(
//                                nodeId,
//                                DATA_ITEM_RECEIVED_PATH,
//                                payload
//                            )
//                                .await()
//                            Log.d(TAG, "Message sent successfully")
                        } catch (cancellationException: CancellationException) {
                            throw cancellationException
                        } catch (exception: Exception) {
                            Log.d(TAG, "Message failed")
                        }
                    }
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        Log.d(TAG, "DT MR ${messageEvent.path}")

        when (messageEvent.path) {
            START_ACTIVITY_PATH -> {
                startActivity(
                    Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        private const val TAG = "DataLayerService"

        const val START_ACTIVITY_PATH = "/start-activity"
        const val DATA_ITEM_RECEIVED_PATH = "/data-item-received"
        const val MOBILE_PATH = "/mobile"
        private const val MOBILE_KEY = "key_mobile"
    }
}