package com.ugsm.secretpresent.dto

data class GiftProductCategoryNotReceivedDto(
    val multipleGiftsCategories: List<GiftListProductCategoryDto>,
    val singleGiftsCategories: List<GiftListProductCategoryDto>
)
