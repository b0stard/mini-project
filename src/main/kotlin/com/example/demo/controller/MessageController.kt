package com.example.demo.controller

import com.example.demo.model.request.CreateMessageRequest
import com.example.demo.model.response.MessageResponse
import com.example.demo.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/messages")
class MessageController(
    private val messageService: MessageService
) {

    @PostMapping
    fun create(@RequestBody request: CreateMessageRequest): ResponseEntity<MessageResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(messageService.create(request))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): MessageResponse {
        return messageService.getById(id)
    }
}