package com.ugsm.secretpresent.dto

import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import java.time.LocalDateTime

data class GiftListGivenProductDto(
    val productId: Long,
    val productName: String,
    val productPrice: Int,
    val confirmedStatus: GiftConfirmedStatus,
    val giverId: Long?,
    val giverNickname: String,
    val sentAt: LocalDateTime?,
)
