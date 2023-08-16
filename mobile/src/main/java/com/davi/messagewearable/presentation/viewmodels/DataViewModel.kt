package com.davi.messagewearable.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.davi.messagewearable.domain.model.MessageApp
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import java.time.LocalDateTime

class DataViewModel(application: Application) : AndroidViewModel(application),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    private val _messageApps = mutableStateListOf<MessageApp>()
    val messages: List<MessageApp> = _messageApps

    init {
        _messageApps.add(MessageApp(LocalDateTime.now(), "Olá", true))
        _messageApps.add(MessageApp(LocalDateTime.now(), "Mundo!", true))
    }

    fun addMessage(message: String, my: Boolean) {
        _messageApps.add(MessageApp(LocalDateTime.now(), message, my))
    }

    private val _textfield = mutableStateOf<String>("")
    var textfield: String = _textfield.value
        set(value) {
            _textfield.value = value
            field = value
        }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { dataEvent ->
            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    when (dataEvent.dataItem.uri.path) {
                        WEAR_PATH -> {
                            DataMapItem.fromDataItem(dataEvent.dataItem)
                                .dataMap
                                .apply {
                                    val msg = "${dataEvent.dataItem.uri.toString()} - " + getString(WEAR_KEY)
                                    if (msg != null) {
                                        _messageApps.add(MessageApp(LocalDateTime.now(), msg, false))
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d(TAG, "message -> ${messageEvent.toString()}")
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {

    }

    companion object {
        const val TAG = "DataViewModel"
        private const val WEAR_PATH = "/wear"
        private const val WEAR_KEY = "key_wear"
    }
}