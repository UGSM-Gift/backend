package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.survey.UserSurvey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSurveyRepository: JpaRepository<UserSurvey, Int> {

    fun findByUserId(userId: Long): UserSurvey

}