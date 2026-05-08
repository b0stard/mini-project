package com.example.demo.mapper

import com.example.demo.entity.NotificationEntity
import com.example.demo.model.response.NotificationResponse

fun NotificationEntity.toResponse(): NotificationResponse =
    NotificationResponse(
        id = id!!,
        userId = user.id!!,
        messageId = message.id!!,
        channel = channel,
        recipient = recipient,
        status = status,
        attemptCount = attemptCount,
        maxAttempts = maxAttempts,
        nextRetryAt = nextRetryAt,
        lastError = lastError,
        createdAt = createdAt,
        sentAt = sentAt
    )