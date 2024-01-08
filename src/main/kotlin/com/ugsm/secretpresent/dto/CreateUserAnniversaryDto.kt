package com.ugsm.secretpresent.dto

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class CreateUserAnniversaryDto(
    @NotBlank(message = "message should not be blank")
    val name: String,
    val date: LocalDate,
    val imageId: Int,
)
