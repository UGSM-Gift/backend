package com.ugsm.secretpresent.dto

data class NaverShoppingCategoryDto(
    val id: Int,
    val name: String?,
    val children: List<NaverShoppingCategoryDto>?
)
