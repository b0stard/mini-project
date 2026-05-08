package com.example.demo.model.response

import com.example.demo.enum.MessageStatus
import com.example.demo.enum.NotificationChannel
import java.util.UUID

data class MessageResponse(
    val id: UUID,
    val templateCode: String,
    val channel: NotificationChannel,
    val subject: String?,
    val body: String,
    val status: MessageStatus
)