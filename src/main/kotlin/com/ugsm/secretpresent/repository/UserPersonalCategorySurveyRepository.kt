package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.personalcategory.UserPersonalCategorySurvey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserPersonalCategorySurveyRepository: JpaRepository<UserPersonalCategorySurvey, Int> {

    fun findByUserId(userId: Long): UserPersonalCategorySurvey

}