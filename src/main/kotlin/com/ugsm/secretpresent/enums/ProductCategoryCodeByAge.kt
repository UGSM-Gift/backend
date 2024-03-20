package com.ugsm.secretpresent.enums

enum class ProductCategoryCodeByAge(
    val code: Int, val categoryName: String
) {
    TEENAGE_FEMALE(10016416, "10대 여성"),
    TEENAGE_MALE(10016417, "10대 남성"),
    TWEN_TO_THI_FEMALE(10016418, "2030대 여성"),
    TWEN_TO_THI_MALE(10016452, "2030대 남성"),
    FOUR_TO_FIFTH_FEMALE(10016453, "4050대 여성"),
    FOUR_TO_FIFTH_MALE(10016454, "4050대 남성")
}