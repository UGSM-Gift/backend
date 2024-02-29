package com.ugsm.secretpresent.dto.user

import com.ugsm.secretpresent.enums.Gender
import com.ugsm.secretpresent.enums.OAuth2Type
import java.time.LocalDate

data class OAuthUserInfo(
        val oAuthType: OAuth2Type,
        val oAuthId: String,
        val nickname: String? = null,
        val profileImgUrl: String? = null,
        val email: String? = null,
        val gender: Gender? = null,
        val mobile: String? = null,
        val birthdate: LocalDate? = null
)