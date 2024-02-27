package com.ugsm.secretpresent.dto

data class PersonalCategoryQuestionWithChoicesDto(
    val id: Long,
    val content: String,
    val hasMultipleChoices: Boolean,
    val choices: List<PersonalCategoryQuestionChoiceDto>
)
