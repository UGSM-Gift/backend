package com.ugsm.secretpresent.dto.user

import com.ugsm.secretpresent.enums.Gender
import java.time.LocalDate

data class UserInfo(
    val id: Long,
    val name: String? = null,
    val loginType: String? = null,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val gender: Gender? = null,
    val mobile: String? = null,
    val birthdate: LocalDate? = null,
    val email: String? = null,
    val mobileVerified: Boolean
)