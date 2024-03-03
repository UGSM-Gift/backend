package com.ugsm.secretpresent.dto

import com.ugsm.secretpresent.enums.GiftCategoryReceiptType

data class GiftListProductCategoryDto(
    val id: Int,
    val productCategoryId: Int,
    val productCategoryName: String,
    val receiptType: GiftCategoryReceiptType,
    val products: List<GiftListProductDto>
)

data class GiftListProductDto(
    val id: Int?,
    val productId: Long,
    val productName: String,
    val productPrice: Int,
)
