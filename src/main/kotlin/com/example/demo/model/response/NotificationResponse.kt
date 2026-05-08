package com.example.demo.model.response

import com.example.demo.enum.NotificationChannel
import com.example.demo.enum.NotificationStatus
import java.time.Instant
import java.util.UUID

data class NotificationResponse(
    val id: UUID,
    val userId: UUID,
    val messageId: UUID,
    val channel: NotificationChannel,
    val recipient: String,
    val status: NotificationStatus,
    val attemptCount: Int,
    val maxAttempts: Int,
    val nextRetryAt: Instant?,
    val lastError: String?,
    val createdAt: Instant,
    val sentAt: Instant?
)