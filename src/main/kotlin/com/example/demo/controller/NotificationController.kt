package com.example.demo.controller

import com.example.demo.model.request.CreateNotificationRequest
import com.example.demo.model.response.NotificationResponse
import com.example.demo.service.NotificationService
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
@RequestMapping("/api/v1/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {

    @PostMapping
    fun create(
        @RequestBody request: CreateNotificationRequest
    ): ResponseEntity<NotificationResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(notificationService.create(request))
    }

    @GetMapping
    fun getAll(): List<NotificationResponse> {
        return notificationService.getAll()
    }

    @PostMapping("/{id}/cancel")
    fun cancel(@PathVariable id: UUID): NotificationResponse {
        return notificationService.cancel(id)
    }
    @PostMapping("/{id}/send")
    fun send(@PathVariable id: UUID): NotificationResponse {
        return notificationService.send(id)
    }
}