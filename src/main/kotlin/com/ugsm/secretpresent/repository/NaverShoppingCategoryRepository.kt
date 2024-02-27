package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.NaverShoppingCategory
import org.springframework.data.jpa.repository.JpaRepository

interface NaverShoppingCategoryRepository: JpaRepository<NaverShoppingCategory, Int> {
    fun findByParentCategoryIsNull(): MutableList<NaverShoppingCategory>

    fun findByActiveTrueAndParentCategoryIsNullAndIdNotIn(ids: List<Int>): List<NaverShoppingCategory>
}