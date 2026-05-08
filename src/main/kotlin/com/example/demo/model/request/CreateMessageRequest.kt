package com.example.demo.model.request

import com.example.demo.enum.NotificationChannel

data class CreateMessageRequest(
    val templateCode: String,
    val channel: NotificationChannel,
    val subject: String?,
    val body: String
)