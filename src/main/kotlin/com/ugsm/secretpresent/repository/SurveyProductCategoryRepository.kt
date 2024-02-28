package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.survey.SurveyGPTProductCategory
import com.ugsm.secretpresent.model.survey.SurveyProductCategory
import org.springframework.data.jpa.repository.JpaRepository

interface SurveyProductCategoryRepository:JpaRepository<SurveyProductCategory, Long> {
    fun findBySurveyId(surveyId: Int):List<SurveyGPTProductCategory>
}