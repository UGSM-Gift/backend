package com.ugsm.secretpresent.dto

import com.ugsm.secretpresent.enums.PersonalCategoryType


data class CreateSurveyDto(
    val anniversaryId: Int,
    val answeredCategories: List<PersonalCategoryWithQuestionsAndAnswers>,
    val questionsWithAnswers: List<PersonalCategoryQuestionWithAnswers>
)

data class PersonalCategoryWithQuestionsAndAnswers(
    val id: Int,
    val type: PersonalCategoryType? = null,
    val otherName: String?,
)

data class PersonalCategoryQuestionWithAnswers(
    val questionId: Int,
    val answerId: Int?,
    val otherAnswer: String?
)
