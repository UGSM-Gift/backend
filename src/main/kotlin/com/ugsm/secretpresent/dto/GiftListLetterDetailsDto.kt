package com.ugsm.secretpresent.dto

import java.time.LocalDateTime

data class GiftListLetterDetailsDto(
    val id: Int,
    val giverId: Long?,
    val giverNickname: String,
    val packageImgName: String,
    val productId: Long,
    val productName: String,
    val productPrice: Int,
    val letterMessage: String,
    val writtenAt: LocalDateTime?
)
