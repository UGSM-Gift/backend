package com.ugsm.secretpresent.dto.user

data class NicknameValidationDto(
    val nickname: String,
    val valid: Boolean,
    val reason: String? = null,
)
