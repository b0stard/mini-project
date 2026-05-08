package com.example.demo.mapper

import com.example.demo.entity.UserEntity
import com.example.demo.model.response.UserResponse

fun UserEntity.toResponse(): UserResponse =
    UserResponse(
        id = id!!,
        email = email,
        phone = phone,
        isActive = true,
        password = passwordHash
    )