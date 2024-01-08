package com.ugsm.secretpresent.dto

data class NicknameValidationDto(
    val nickname: String,
    val valid: Boolean,
    val reason: String? = null,
)
