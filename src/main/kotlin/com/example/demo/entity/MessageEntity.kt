package com.example.demo.entity

import com.example.demo.enum.MessageStatus
import com.example.demo.enum.NotificationChannel
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "messages")
class MessageEntity(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false)
    val templateCode: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val channel: NotificationChannel,

    @Column
    val subject: String? = null,

    @Column(nullable = false, columnDefinition = "text")
    val body: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: MessageStatus = MessageStatus.CREATED,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()
)