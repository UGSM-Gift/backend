package com.ugsm.secretpresent.dto

data class CreateLetterDto(
    val productCategoryId: Int,
    val productId: Long,
    val imageFileName: String,
    val message: String,
)
