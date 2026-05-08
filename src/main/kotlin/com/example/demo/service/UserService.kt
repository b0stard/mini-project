package com.example.demo.service

import com.example.demo.config.passwordEncoder
import com.example.demo.entity.UserEntity
import com.example.demo.mapper.toResponse
import com.example.demo.model.request.ChangePasswordRequest
import com.example.demo.model.request.CreateUserRequest
import com.example.demo.model.request.UpdateUserRequest
import com.example.demo.model.response.UserResponse
import com.example.demo.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime

import java.util.UUID


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun create(request: CreateUserRequest): UserResponse {
        val fundUser = userRepository.findByEmail(request.email)
        if (fundUser != null) {
            throw IllegalArgumentException("User already exists")
        }
        val passwordHashed = passwordEncoder.encode(request.password)
        val userEntity = UserEntity(
            email = request.email,
            phone = request.phone,
            passwordHash = passwordHashed
        )
        return userRepository.save(userEntity).toResponse()
    }

    @Transactional
    fun changePassword(userId: UUID, request: ChangePasswordRequest) {
        val user =
            userRepository.findById(userId).orElseThrow { EntityNotFoundException("User not found with id $userId") }

        val matches = passwordEncoder.matches(
            request.oldPassword,
            user.passwordHash
        )
        if (!matches) {
            throw IllegalArgumentException("Passwords do not match")
        }
        user.passwordHash = passwordEncoder.encode(request.newPassword)
        user.updatedAt = Instant.now()
    }

    @Transactional
    fun update(id: UUID, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("User not found") }

        val userWithSameEmail = userRepository.findByEmail(request.email)

        if (userWithSameEmail != null && userWithSameEmail.id != user.id) {
            throw IllegalArgumentException("User with this email already exists")
        }

        user.email = request.email
        user.phone = request.phone
        user.updatedAt = Instant.now()

        return user.toResponse()
    }

    @Transactional
    fun delete(id: UUID) {
        val user = userRepository.findById(id)
            .orElseThrow { EntityNotFoundException("User not found with id $id") }

        return userRepository.delete(user)
    }

    fun getAll(): List<UserResponse> {
        return userRepository.findAll().map { it.toResponse() }
    }
}
