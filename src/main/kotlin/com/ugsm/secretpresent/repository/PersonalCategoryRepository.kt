package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.enums.PersonalCategoryType
import com.ugsm.secretpresent.model.personalcategory.PersonalCategory
import org.springframework.data.jpa.repository.JpaRepository

interface PersonalCategoryRepository: JpaRepository<PersonalCategory, Int>{

    fun findByTypeOrderByViewOrderAsc(type: PersonalCategoryType): List<PersonalCategory>
}