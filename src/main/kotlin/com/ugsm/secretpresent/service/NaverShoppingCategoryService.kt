package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.NaverShoppingCategoryDto
import com.ugsm.secretpresent.model.NaverShoppingCategory
import com.ugsm.secretpresent.repository.NaverShoppingCategoryRepository
import com.ugsm.secretpresent.repository.NaverShoppingCategoryRepositorySupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NaverShoppingCategoryService(
    @Autowired
    var repository: NaverShoppingCategoryRepository,
    @Autowired
    var support: NaverShoppingCategoryRepositorySupport
){
    fun getAll(): List<NaverShoppingCategoryDto> {
        val categories = support.getAll()
        val categoriesDto = categories.map { category ->
            val children = category.childCategories?.map{
                NaverShoppingCategoryDto(
                    id=it.id,
                    name=it.name,
                    children = null
                )
            }

            NaverShoppingCategoryDto(
                id=category.id,
                name=category.name,
                children = children
            )
        }
        return categoriesDto
    }

    fun test(): List<NaverShoppingCategory> {
        val items = support.getAll()

        return items
    }
}