package com.ugsm.secretpresent.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.ugsm.secretpresent.enums.NotificationType
import java.time.LocalDateTime

data class NotificationDto(
    val id: Long?,
    val content: String,
    val type: NotificationType,
    val referenceId: Int?,
    val read: Boolean,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val sentAt: LocalDateTime
)
