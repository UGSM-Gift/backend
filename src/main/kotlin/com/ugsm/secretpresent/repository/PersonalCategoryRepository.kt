package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.enums.PersonalCategoryType
import com.ugsm.secretpresent.model.personalcategory.PersonalCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonalCategoryRepository: JpaRepository<PersonalCategory, Int>{

    fun findByTypeOrderByViewOrderAsc(type: PersonalCategoryType): List<PersonalCategory>
}