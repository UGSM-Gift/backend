package com.ugsm.secretpresent.dto.personalcategory

data class PersonalCategoryQuestionWithChoicesDto(
    val id: Long,
    val content: String,
    val hasMultipleChoices: Boolean,
    val choices: List<PersonalCategoryQuestionChoiceDto>
)
