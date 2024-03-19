package com.ugsm.secretpresent.dto.giftlist

import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import java.time.LocalDate

data class CreateGiftListDto(
    val imageFileName: String,
    val availableAt: LocalDate,
    val expiredAt: LocalDate,
    val anniversaryId: Int,
    val categoriesWithProducts: List<SelectedCategoryWithProducts>
)

data class SelectedCategoryWithProducts(
    val categoryId: Int,
    val productIds: List<Long>,
    val receiptType: GiftCategoryReceiptType?
)
