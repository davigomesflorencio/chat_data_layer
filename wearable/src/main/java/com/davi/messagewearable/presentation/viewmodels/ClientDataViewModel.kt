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

class ClientDataViewModel(application: Application) : AndroidViewModel(application),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

     val _messages = mutableStateListOf<MessageApp>()
    val messages: List<MessageApp> = _messages

   fun addMessage(message: String, my: Boolean) {
        _messages.add(MessageApp(LocalDateTime.now(), message, my))
    }

    init {
        _messages.add(MessageApp(LocalDateTime.now(), "Olá", true))
        _messages.add(MessageApp(LocalDateTime.now(), "Mundo!", true))
    }

    private val _mobileDeviceConnected = mutableStateOf<Boolean>(false)
    val mobileDeviceConnected: Boolean = _mobileDeviceConnected.value

    private val _textfield = mutableStateOf<String>("")
    var textfield: String = _textfield.value
        set(value) {
            _textfield.value = value
            field = value
        }

    override fun onDataChanged(p0: DataEventBuffer) {
        p0.map { dataEvent ->
            Log.d(TAG, "${dataEvent.dataItem.uri.toString()}")
        }

        p0.forEach { dataEvent ->
            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    when (dataEvent.dataItem.uri.path) {
                        MOBILE_PATH -> {
                            DataMapItem.fromDataItem(dataEvent.dataItem)
                                .dataMap
                                .apply {
                                    val msg = "${dataEvent.dataItem.uri} - " + getString(MOBILE_KEY)
                                    if (msg != null) {
                                        _messages.add(MessageApp(LocalDateTime.now(), msg, false))
                                    }
                                }
                        }
                    }
                }

                DataEvent.TYPE_CHANGED -> {
                    when (dataEvent.dataItem.uri.path) {
                        MOBILE_PATH -> {
                            DataMapItem.fromDataItem(dataEvent.dataItem)
                                .dataMap
                                .apply {
                                    val msg = getString(MOBILE_KEY)
                                    if (msg != null) {
                                        _messages.add(MessageApp(LocalDateTime.now(), msg, false))
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    override fun onMessageReceived(p0: MessageEvent) {
        Log.d(TAG, "MR ${p0.path}")
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
        Log.d(TAG, "CC ${p0.name}")
    }

    companion object {
        private const val TAG = "ClientViewModel"
        private const val MOBILE_PATH = "/mobile"
        private const val MOBILE_KEY = "key_mobile"
    }
}