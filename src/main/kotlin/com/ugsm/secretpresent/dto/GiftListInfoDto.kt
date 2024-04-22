package com.ugsm.secretpresent.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class GiftListInfoDto(
    val giftListId:Int,
    val takerId: Long,
    val takerNickname: String,
    val giftListImageUrl: String,
    val anniversaryName: String,
    val anniversaryImageUrl: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val availableAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val expiredAt: LocalDateTime,
    val multipleGiftsCategories: List<GiftListProductCategoryDto>,
    val singleGiftCategories: List<GiftListProductCategoryDto>,
    var selectedProductsNumber: Int,
    var receivedProductsNumber: Int,
)
