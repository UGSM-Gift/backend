package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.NaverShoppingCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NaverShoppingCategoryRepository: JpaRepository<NaverShoppingCategory, Int> {
    fun findByParentCategoryIsNull(): MutableList<NaverShoppingCategory>
}