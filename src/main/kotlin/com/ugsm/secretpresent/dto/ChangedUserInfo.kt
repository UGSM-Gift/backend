package com.ugsm.secretpresent.dto

import com.ugsm.secretpresent.enums.Gender
import jakarta.validation.constraints.Email
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.validation.annotation.Validated
import java.time.LocalDate

data class ChangedUserInfo(
    val name: String? = null,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    var mobile: String? = null,
    @DateTimeFormat(pattern = "yyyy-MM-dd") val birthdate: LocalDate? = null,
    val gender: Gender? = null,
    @Email @Validated val email: String? = null,
)