package com.example.demo.entity

import com.example.demo.enum.NotificationChannel
import com.example.demo.enum.NotificationStatus
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(
    name = "notifications",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_notifications_user_idempotency_key",
            columnNames = ["user_id", "idempotency_key"]
        )
    ]
)
class NotificationEntity(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "message_id", nullable = false)
    val message: MessageEntity,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val channel: NotificationChannel,

    @Column(nullable = false)
    val recipient: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: NotificationStatus = NotificationStatus.CREATED,

    @Column(name = "idempotency_key", nullable = false)
    val idempotencyKey: String,

    @Column(nullable = false)
    var attemptCount: Int = 0,

    @Column(nullable = false)
    val maxAttempts: Int = 5,

    @Column
    var nextRetryAt: Instant? = null,

    @Column(columnDefinition = "text")
    var lastError: String? = null,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now(),

    @Column
    var sentAt: Instant? = null
)