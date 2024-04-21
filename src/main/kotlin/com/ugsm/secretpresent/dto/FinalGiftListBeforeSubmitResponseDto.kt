package com.ugsm.secretpresent.dto


data class CategoryWithProductsDto(
    val categoryId: Int,
    val categoryName: String,
    val products: List<ProductDto>
)

data class FinalGiftListBeforeSubmitResponseDto(
    val single: List<CategoryWithProductsDto>,
    val multiple: List<CategoryWithProductsDto>
)