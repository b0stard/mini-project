package com.example.demo.model.request

import com.example.demo.enum.NotificationChannel
import java.util.UUID

data class CreateNotificationRequest(
    val userId: UUID,
    val channel: NotificationChannel,
    val recipient: String,
    val templateCode: String,
    val variables: Map<String, String>,
    val idempotencyKey: String
)