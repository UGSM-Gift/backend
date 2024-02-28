package com.ugsm.secretpresent.dto.personalcategory

data class PersonalCategoryWithQuestionsDto(
    val category: PersonalCategoryDto,
    val questions: List<PersonalCategoryQuestionWithChoicesDto>
)