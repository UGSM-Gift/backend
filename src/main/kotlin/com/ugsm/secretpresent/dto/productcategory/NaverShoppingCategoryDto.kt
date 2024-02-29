package com.ugsm.secretpresent.dto.productcategory

data class NaverShoppingCategoryDto(
    val id: Int,
    val name: String?,
    val parentId: Int?,
    val children: List<NaverShoppingCategoryDto>?
)
