package com.ugsm.secretpresent.dto

import java.time.LocalDateTime

data class GiftListLetterDto(
    val id: Int?,
    val giverId: Long?,
    val giverNickname: String,
    val giftListPackageImgUrl: String,
    val writtenAt: LocalDateTime?
)
