package com.example.demo.service

import com.example.demo.entity.NotificationEntity
import com.example.demo.enum.NotificationChannel
import com.example.demo.repository.NotificationSenderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailNotificationSenderService @Autowired constructor(

    private val mailSender: JavaMailSender
) : NotificationSenderRepository {

    override fun send(notification: NotificationEntity) {
        if (notification.channel != NotificationChannel.EMAIL) {
            throw IllegalArgumentException("Unsupported channel: ${notification.channel}")
        }

        val message = SimpleMailMessage().apply {
            setTo(notification.recipient)
            subject = notification.message.subject ?: "Notification"
            text = notification.message.body
        }

        mailSender.send(message)
    }
}