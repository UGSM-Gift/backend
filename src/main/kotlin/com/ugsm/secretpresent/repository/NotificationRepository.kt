package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.Notification
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface NotificationRepository: JpaRepository<Notification, Long> {
    fun findFirstByUserIdAndDeliveredFalseAndReservedAtLessThanOrderByIdAsc(userId: Long, reservedAt: LocalDateTime): Notification?
    fun findByUserIdAndReservedAtLessThanOrderByIdAsc(userId: Long, reservedAt: LocalDateTime): List<Notification>
}