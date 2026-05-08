package com.example.demo.service

import com.example.demo.entity.MessageEntity
import com.example.demo.mapper.toResponse
import com.example.demo.model.request.CreateMessageRequest
import com.example.demo.model.response.MessageResponse
import com.example.demo.repository.MessageRepository
import com.example.demo.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MessageService(
    private val messageRepository: MessageRepository
) {
    @Transactional
    fun create(request: CreateMessageRequest): MessageResponse {
        val message = MessageEntity(
            templateCode = request.templateCode,
            channel = request.channel,
            subject = request.subject,
            body = request.body
        )
        return messageRepository.save(message).toResponse()
    }

    fun getById(id: UUID): MessageResponse {
        val message = messageRepository.findById(id)
            .orElseThrow { RuntimeException("Message not found") }
        return message.toResponse()
    }

    @Transactional
    fun delete(id: UUID) {
        val message = messageRepository.findById(id)
            .orElseThrow { RuntimeException("Message not found") }
        messageRepository.delete(message)
    }
}