package com.ugsm.secretpresent.dto

data class CreateLetterDto(
    val id: Int,
    val productCategoryId: Int,
    val productId: Long,
    val letterImgUrl: String?,
    val message: String,
)
