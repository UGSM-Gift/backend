package com.ugsm.secretpresent.dto

data class RecommendedProductByAgeDto(
    val lessThan10K: List<RecommendedProductDto>,
    val between10KTo30K: List<RecommendedProductDto>,
    val between30KTo50K: List<RecommendedProductDto>,
    val moreThan50K: List<RecommendedProductDto>,
)

data class RecommendedProductDto(
    val id: Long,
    val name: String,
    val imageUrl: String?,
    val buyingUrl: String,
    val price: Int,
    val dibbed: Boolean
)
