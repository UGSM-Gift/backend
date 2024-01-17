package com.ugsm.secretpresent.dto

import com.ugsm.secretpresent.model.NaverShoppingCategory

data class NaverShoppingCategoryDto(
    val id: Int,
    val name: String,
    val children: List<NaverShoppingCategoryDto>?
)
