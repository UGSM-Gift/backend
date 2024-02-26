package com.ugsm.secretpresent.dto

data class PersonalCategoryWithQuestionsDto(
    val category: PersonalCategoryDto,
    val questions: List<PersonalCategoryQuestionWithChoicesDto>
)