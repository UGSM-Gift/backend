package com.ugsm.secretpresent.dto.giftlist

import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import java.time.LocalDateTime

data class GiftListDetailDto(
    val takerNickname:String,
    val anniversaryTitle: String,
    val createdAt: LocalDateTime?,
    val expiredAt: LocalDateTime?,
    var categories: List<GiftListCategoryWithSelectedProducts> = emptyList()
)


data class GiftListCategoryWithSelectedProducts(
    val id: Int,
    val name: String,
    val products: List<ProductInfo>,
    val receiptType: GiftCategoryReceiptType
)

data class ProductInfo(
    val id: Long,
    val name: String,
    val price: Int
)