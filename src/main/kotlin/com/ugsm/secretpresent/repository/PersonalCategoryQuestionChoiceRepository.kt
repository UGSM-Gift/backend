package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.personalcategory.PersonalCategoryQuestionChoice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonalCategoryQuestionChoiceRepository:JpaRepository<PersonalCategoryQuestionChoice, Int> {

}
