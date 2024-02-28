package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.survey.SurveyGPTProductCategory
import org.springframework.data.jpa.repository.JpaRepository

interface SurveyGPTProductCategoryRepository:JpaRepository<SurveyGPTProductCategory, Long> {

    fun findBySurveyId(surveyId: Int):List<SurveyGPTProductCategory>
}