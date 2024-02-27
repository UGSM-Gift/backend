package com.ugsm.secretpresent.dto

data class ProductCategoryDto(
    val id: Int,
    val name: String,
    val depth: Int,
    val parentCategoryId: Int?,
    val firstParentId: Int,
    val firstParentName: String
)