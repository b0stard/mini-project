package com.example.demo.service

import com.example.demo.entity.MessageEntity
import com.example.demo.entity.NotificationEntity
import com.example.demo.entity.OutboxEventEntity
import com.example.demo.enum.NotificationStatus
import com.example.demo.mapper.toResponse
import com.example.demo.model.request.CreateNotificationRequest
import com.example.demo.model.response.NotificationResponse
import com.example.demo.repository.MessageRepository
import com.example.demo.repository.NotificationRepository
import com.example.demo.repository.OutboxEventRepository
import com.example.demo.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class NotificationService(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val notificationRepository: NotificationRepository,
    private val emailNotificationSenderService: EmailNotificationSenderService,
    private val outboxEventRepository: OutboxEventRepository
) {
    @Transactional
    fun create(request: CreateNotificationRequest): NotificationResponse {
        val existing = notificationRepository
            .findByUserIdAndIdempotencyKey(request.userId, request.idempotencyKey)

        if (existing != null) {
            return existing.toResponse()
        }

        val user = userRepository.findById(request.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val message = MessageEntity(
            templateCode = request.templateCode,
            channel = request.channel,
            subject = null,
            body = request.variables.toString()
        )

        val savedMessage = messageRepository.save(message)

        val notification = NotificationEntity(
            user = user,
            message = savedMessage,
            channel = request.channel,
            recipient = request.recipient,
            idempotencyKey = request.idempotencyKey,
            status = NotificationStatus.CREATED
        )

        val savedNotification = notificationRepository.save(notification)

        val outboxEvent = OutboxEventEntity(
            aggregateType = "NOTIFICATION",
            aggregateId = savedNotification.id!!,
            eventType = "NOTIFICATION_CREATED",
            payload = """
            {
              "notificationId": "${savedNotification.id}",
              "channel": "${savedNotification.channel}",
              "recipient": "${savedNotification.recipient}"
            }
        """.trimIndent()
        )

        outboxEventRepository.save(outboxEvent)

        return savedNotification.toResponse()
    }

    fun getAll(): List<NotificationResponse> {
        return notificationRepository.findAll()
            .map { it.toResponse() }
    }

    @Transactional
    fun cancel(id: UUID): NotificationResponse {
        val notification = notificationRepository.findById(id)
            .orElseThrow { RuntimeException("Message not found") }
        if (notification.status != NotificationStatus.CANCELLED && notification.status != NotificationStatus.QUEUED) {
            throw RuntimeException("\"Only CREATED or QUEUED notifications can be cancelled")
        }
        notification.status = NotificationStatus.CANCELLED
        notification.status = NotificationStatus.QUEUED

        return notificationRepository.save(notification).toResponse()
    }
    @Transactional
    fun send(id: UUID): NotificationResponse {
        val notification = notificationRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Notification not found") }

        if (notification.status == NotificationStatus.CANCELLED) {
            throw IllegalStateException("Cancelled notification cannot be sent")
        }

        if (notification.status == NotificationStatus.SENT) {
            return notification.toResponse()
        }

        notification.status = NotificationStatus.SENDING
        notification.updatedAt = Instant.now()

        try {
            emailNotificationSenderService.send(notification)

            notification.status = NotificationStatus.SENT
            notification.sentAt = Instant.now()
            notification.updatedAt = Instant.now()
            notification.lastError = null
        } catch (ex: Exception) {
            notification.status = NotificationStatus.FAILED
            notification.attemptCount += 1
            notification.lastError = ex.message
            notification.updatedAt = Instant.now()
        }

        return notification.toResponse()
    }
}