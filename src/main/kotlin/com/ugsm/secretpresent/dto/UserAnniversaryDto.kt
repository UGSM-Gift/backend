package com.ugsm.secretpresent.dto

import java.time.LocalDate

data class UserAnniversaryDto(
    val id: Long?,
    val name: String,
    val date: LocalDate,
    val imageUrl: String,
)
