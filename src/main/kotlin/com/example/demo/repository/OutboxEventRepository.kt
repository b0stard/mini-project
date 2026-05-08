package com.example.demo.repository

import com.example.demo.entity.OutboxEventEntity
import com.example.demo.enum.OutboxStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OutboxEventRepository : JpaRepository<OutboxEventEntity, UUID> {
    fun findTop20ByStatusOrderByCreatedAtAsc(status: OutboxStatus): List<OutboxEventEntity>
}