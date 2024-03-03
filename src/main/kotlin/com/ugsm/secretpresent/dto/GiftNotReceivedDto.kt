package com.ugsm.secretpresent.dto

import java.time.LocalDate

data class GiftListInfoDto(
    val giftListId:Int,
    val takerId: Long,
    val takerNickname: String,
    val anniversaryName: String,
    val anniversaryImageUrl: String,
    val availableAt: LocalDate,
    val expiredAt: LocalDate,
    val multipleGiftsCategories: List<GiftListProductCategoryDto>,
    val singleGiftsCategories: List<GiftListProductCategoryDto>
)
