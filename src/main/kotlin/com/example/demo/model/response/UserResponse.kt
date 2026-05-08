package com.example.demo.model.response

import java.util.UUID

data class UserResponse(
    val id: UUID,
    var email: String,
    var password: String,
    var phone: String?,
    val isActive: Boolean
)