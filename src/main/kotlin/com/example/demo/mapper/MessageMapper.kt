package com.example.demo.mapper

import com.example.demo.entity.MessageEntity
import com.example.demo.model.response.MessageResponse

fun MessageEntity.toResponse(): MessageResponse =
    MessageResponse(
        id = id!!,
        templateCode = templateCode,
        channel = channel,
        subject = subject,
        body = body,
        status = status
    )