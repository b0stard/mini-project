package com.example.demo.repository

import com.example.demo.entity.NotificationEntity
import org.springframework.stereotype.Repository

@Repository
interface NotificationSenderRepository {
    fun send(notification: NotificationEntity)
}