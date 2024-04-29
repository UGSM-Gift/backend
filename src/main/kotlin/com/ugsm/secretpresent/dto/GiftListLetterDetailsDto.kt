package com.ugsm.secretpresent.dto

import com.fasterxml.jackson.annotation.JsonFormat
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val writtenAt: LocalDateTime?
)
