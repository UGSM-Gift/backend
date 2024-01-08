package com.ugsm.secretpresent.dto

import com.ugsm.secretpresent.enums.Gender
import java.time.LocalDate

data class ChangedUserInfo(
    val name: String? = null,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val mobile: String? = null,
    val birthdate: LocalDate? = null,
    val gender: Gender? = null,
    val email: String? = null,
)