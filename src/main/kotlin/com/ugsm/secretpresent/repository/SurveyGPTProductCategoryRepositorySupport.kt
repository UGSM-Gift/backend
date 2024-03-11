package com.ugsm.secretpresent.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ugsm.secretpresent.model.survey.QSurveyGPTProductCategory
import com.ugsm.secretpresent.model.survey.SurveyGPTProductCategory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository


@Repository
class SurveyGPTProductCategoryRepositorySupport(
    private val queryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(SurveyGPTProductCategory::class.java) {

    fun findBySurveyId(surveyId: Int): List<SurveyGPTProductCategory> {
        val root = QSurveyGPTProductCategory.surveyGPTProductCategory

        return queryFactory
            .selectFrom(root)
            .leftJoin(root.productCategory).fetchJoin()
            .where(root.survey.id.eq(surveyId))
            .fetch()
    }
}