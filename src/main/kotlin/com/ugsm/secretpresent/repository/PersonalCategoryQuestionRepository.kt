package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.personalcategory.PersonalCategoryQuestion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonalCategoryQuestionRepository:JpaRepository<PersonalCategoryQuestion, Int>
