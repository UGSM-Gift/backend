package com.ugsm.secretpresent.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class NotificationDto(
    val id: Long?,
    val content: String,
    val redirectUrl: String?,
    val read: Boolean,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val sentAt: LocalDateTime
)
