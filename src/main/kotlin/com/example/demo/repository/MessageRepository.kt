package com.example.demo.repository

import com.example.demo.entity.MessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MessageRepository : JpaRepository<MessageEntity, UUID>