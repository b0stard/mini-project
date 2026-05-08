package com.example.demo.controller

import com.example.demo.model.request.ChangePasswordRequest
import com.example.demo.model.request.CreateUserRequest
import com.example.demo.model.request.UpdateUserRequest
import com.example.demo.model.response.UserResponse
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun create(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.create(request))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: UpdateUserRequest
    ): UserResponse {
        return userService.update(id, request)
    }

    @PatchMapping("/{id}/password")
    fun changePassword(
        @PathVariable id: UUID,
        @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<Void> {
        userService.changePassword(id, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAll(): List<UserResponse> {
        return userService.getAll()
    }
}