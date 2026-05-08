package com.example.demo.service

import com.example.demo.enum.NotificationStatus
import com.example.demo.enum.OutboxStatus
import com.example.demo.repository.NotificationRepository
import com.example.demo.repository.NotificationSenderRepository
import com.example.demo.repository.OutboxEventRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class OutboxWorker(
    private val outboxEventRepository: OutboxEventRepository,
    private val notificationRepository: NotificationRepository,
    private val emailNotificationSenderService: EmailNotificationSenderService
) {

    @Scheduled(fixedDelay = 5000)
    @Transactional
    fun process() {
        val events = outboxEventRepository
            .findTop20ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW)

        events.forEach { event ->
            try {
                val notification = notificationRepository.findById(event.aggregateId)
                    .orElseThrow { IllegalArgumentException("Notification not found") }

                if (notification.status == NotificationStatus.CANCELLED) {
                    event.status = OutboxStatus.PUBLISHED
                    event.publishedAt = Instant.now()
                    event.updatedAt = Instant.now()
                    return@forEach
                }

                notification.status = NotificationStatus.SENDING
                notification.updatedAt = Instant.now()

                emailNotificationSenderService.send(notification)

                notification.status = NotificationStatus.SENT
                notification.sentAt = Instant.now()
                notification.lastError = null
                notification.updatedAt = Instant.now()

                event.status = OutboxStatus.PUBLISHED
                event.publishedAt = Instant.now()
                event.lastError = null
                event.updatedAt = Instant.now()

            } catch (ex: Exception) {
                event.retryCount += 1
                event.lastError = ex.message
                event.updatedAt = Instant.now()

                if (event.retryCount >= event.maxRetries) {
                    event.status = OutboxStatus.FAILED

                    notificationRepository.findById(event.aggregateId)
                        .ifPresent { notification ->
                            notification.status = NotificationStatus.FAILED
                            notification.lastError = ex.message
                            notification.updatedAt = Instant.now()
                        }
                }
            }
        }
    }
}