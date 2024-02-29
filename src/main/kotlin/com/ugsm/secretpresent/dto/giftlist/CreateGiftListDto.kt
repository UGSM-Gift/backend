package com.ugsm.secretpresent.dto.giftlist

import com.fasterxml.jackson.annotation.JsonFormat
import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import java.time.LocalDateTime

data class CreateGiftListDto(
    val packageImgName: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    val expiredAt: LocalDateTime,
    val anniversaryId: Int,
    val categoriesWithProducts: List<SelectedCategoryWithProducts>
)

data class SelectedCategoryWithProducts(
    val categoryId: Int,
    val productIds: List<Long>,
    val receiptType: GiftCategoryReceiptType
)
