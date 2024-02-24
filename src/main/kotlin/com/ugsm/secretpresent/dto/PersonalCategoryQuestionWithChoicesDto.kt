package com.ugsm.secretpresent.dto

data class PersonalCategoryQuestionWithChoicesDto(
    val id: Long,
    val content: String,
    val choices: List<PersonalCategoryQuestionChoiceDto>
)
