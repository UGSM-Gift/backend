package com.ugsm.secretpresent.dto.giftlist

import java.time.LocalDate

data class UpdateGiftListDto(
    val imageFileName: String?,
    val availableAt: LocalDate?,
    val expiredAt: LocalDate?,
    val userAnniversaryId: Int?,
)
