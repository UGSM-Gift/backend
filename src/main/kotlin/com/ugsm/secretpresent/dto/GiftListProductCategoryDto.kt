package com.ugsm.secretpresent.dto

import com.ugsm.secretpresent.enums.GiftCategoryReceiptType

data class GiftListProductCategoryDto(
    val productCategoryId: Int,
    val productCategoryName: String,
    val receiptType: GiftCategoryReceiptType?,
    val products: List<GiftListProductDto>
)

data class GiftListProductDto(
    val id: Long,
    val name: String,
    val price: Int,
    val received: Boolean
)
