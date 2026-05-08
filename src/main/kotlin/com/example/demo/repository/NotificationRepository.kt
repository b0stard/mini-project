package com.example.demo.repository

import com.example.demo.entity.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NotificationRepository : JpaRepository<NotificationEntity, UUID> {
    fun findByUserIdAndIdempotencyKey(userId: UUID, idempotencyKey: String): NotificationEntity?
}