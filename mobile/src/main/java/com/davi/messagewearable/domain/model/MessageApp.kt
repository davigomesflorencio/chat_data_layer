package com.davi.messagewearable.domain.model

import java.time.LocalDateTime

data class MessageApp(val timestamp: LocalDateTime, val message: String, val my: Boolean)