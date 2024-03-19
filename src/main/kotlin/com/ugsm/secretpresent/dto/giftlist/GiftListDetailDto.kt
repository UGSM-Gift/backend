package com.ugsm.secretpresent.dto.giftlist

import com.fasterxml.jackson.annotation.JsonFormat
import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import java.time.LocalDateTime

data class GiftListDetailDto(
    val takerNickname:String,
    val anniversaryTitle: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val availableAt: LocalDateTime?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val expiredAt: LocalDateTime?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,
    var categories: List<GiftListCategoryWithSelectedProducts> = emptyList()
)


data class GiftListCategoryWithSelectedProducts(
    val id: Int,
    val name: String,
    val products: List<ProductInfo>,
    val receiptType: GiftCategoryReceiptType?
)

data class ProductInfo(
    val id: Long,
    val name: String,
    val price: Int
)