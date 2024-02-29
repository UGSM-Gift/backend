package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.productcategory.LeafCategoryDto
import com.ugsm.secretpresent.dto.productcategory.NaverShoppingCategoryDto
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
            val children = category.childCategories.map{
                NaverShoppingCategoryDto(
                    id=it.id,
                    name=it.name,
                    parentId = it.parentCategory?.id,
                    children = null
                )
            }

            NaverShoppingCategoryDto(
                id=category.id,
                name=category.name,
                parentId = category.parentCategory?.id,
                children = children
            )
        }
        return categoriesDto
    }

    fun getAllLeaves():List<LeafCategoryDto> = support.getAllLeaves()

    fun getAllCategories(): List<NaverShoppingCategoryDto> {
        val result = support.getAllCategories()
        val parentCategories = result.map { NaverShoppingCategoryDto(it.id, it.name, null, null) }.toSet()
        return parentCategories.map {parent->
            val children = result.filter{it.id == parent.id && it.childId != null}
                .map{
                    val childId = it.childId as Int
                    NaverShoppingCategoryDto(childId,it.childName, it.id, null)
                }
            NaverShoppingCategoryDto(parent.id,parent.name,null, children)
        }
    }
}