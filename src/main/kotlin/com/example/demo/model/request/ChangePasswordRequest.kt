package com.example.demo.model.request

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)