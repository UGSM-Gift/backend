package com.ugsm.secretpresent.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import java.time.LocalDateTime

data class GiftListGivenProductDto(
    val productId: Long,
    val productName: String,
    val productPrice: Int,
    val confirmedStatus: GiftConfirmedStatus,
    val giverId: Long?,
    val giverNickname: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val sentAt: LocalDateTime?,
    val dibbed: Boolean
)
