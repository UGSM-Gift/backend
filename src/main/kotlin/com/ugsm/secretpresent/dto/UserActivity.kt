package com.ugsm.secretpresent.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class UserActivity(
    val userId: Long?,
    val endpoint: String?,
    val method: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime
)
