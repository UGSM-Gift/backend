package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.personalcategory.SurveyPersonalCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SurveyPersonalCategoryRepository:JpaRepository<SurveyPersonalCategory, Long> {}