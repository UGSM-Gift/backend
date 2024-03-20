package com.ugsm.secretpresent.dto.user

import com.ugsm.secretpresent.Exception.CustomException
import com.ugsm.secretpresent.enums.Gender
import java.time.LocalDate
import java.time.Period

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
){
    fun getAge(): Int {
        if (birthdate == null) throw CustomException(code=50100, message = "생년월일이 존재하지 않습니다.")

        return Period.between(
            birthdate, LocalDate.now()
        ).years
    }
}