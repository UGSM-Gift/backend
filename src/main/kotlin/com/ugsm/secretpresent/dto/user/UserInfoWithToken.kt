package com.ugsm.secretpresent.dto.user

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.ugsm.secretpresent.enums.Gender
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.util.*

data class UserInfoWithToken(
    val id: Long?,
    val name: String?,
    val nickname: String?,
    val profileImageUrl: String?,
    val gender: Gender?,
    val mobile: String?,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val birthdate: LocalDate?,

    val accessToken: String?,
    val refreshToken: String?
)