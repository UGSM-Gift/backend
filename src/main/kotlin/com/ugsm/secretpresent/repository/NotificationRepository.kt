package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository: JpaRepository<Notification, Long> {
    fun findByUserId(userId: Long): List<Notification>
    fun findFirstByUserIdAndReadFalseOrderByIdDesc(userId: Long): Notification?
}