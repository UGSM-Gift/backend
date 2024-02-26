package com.ugsm.secretpresent.dto

import com.ugsm.secretpresent.enums.PersonalCategoryType


data class CreateSurveyDto(
    val anniversaryId: Int,
    val answeredCategories: List<PersonalCategoryWithQuestionsAndAnswers>
)

data class PersonalCategoryWithQuestionsAndAnswers(
    val id: Int,
    val type: PersonalCategoryType? = null,
    val otherName: String?,
    val questionsWithAnswers: List<PersonalCategoryQuestionWithAnswers>?
)

data class PersonalCategoryQuestionWithAnswers(
    val id: Int,
    val answerIds: List<Int>?,
    val otherAnswer: String?
)
