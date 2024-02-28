package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.survey.SurveyPersonalCategoryQuestionAnswer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface SurveyPersonalCategoryQuestionAnswerRepository:JpaRepository<SurveyPersonalCategoryQuestionAnswer, Long> {
}