package com.example.demo.model.request

import java.util.UUID

data class CreateUserRequest(
    val id: UUID? = null,
    var email: String,
    var password: String,
    var phone: String?
)