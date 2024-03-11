package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.survey.SurveyGPTProductCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface SurveyGPTProductCategoryRepository:JpaRepository<SurveyGPTProductCategory, Long> {

    @Query("FROM SurveyGPTProductCategory s join fetch s.productCategory where s.survey.id = :surveyId")
    fun findBySurveyId(surveyId: Int):List<SurveyGPTProductCategory>
}