package com.example.demo.entity

import com.example.demo.enum.OutboxStatus
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "outbox_events")
class OutboxEventEntity(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val aggregateType: String,

    @Column(nullable = false)
    val aggregateId: UUID,

    @Column(nullable = false)
    val eventType: String,

    @Column(nullable = false, columnDefinition = "text")
    val payload: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OutboxStatus = OutboxStatus.NEW,

    @Column(nullable = false)
    var retryCount: Int = 0,

    @Column(nullable = false)
    val maxRetries: Int = 5,

    @Column(columnDefinition = "text")
    var lastError: String? = null,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column
    var publishedAt: Instant? = null,

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now()
)