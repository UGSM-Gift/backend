package com.ugsm.secretpresent.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class GiftListLetterDto(
    val id: Int?,
    val giverId: Long?,
    val giverNickname: String,
    val giftListPackageImgUrl: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val writtenAt: LocalDateTime?
)
