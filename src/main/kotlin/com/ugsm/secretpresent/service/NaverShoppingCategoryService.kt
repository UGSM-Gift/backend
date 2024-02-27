package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.LeafCategoryDto
import com.ugsm.secretpresent.dto.NaverShoppingCategoryDto
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

    fun getAllLeaves():List<LeafCategoryDto> = support.getAllLeaves()

    fun test(): List<Unit> {
        val excludedIds = listOf(
            10030807, 10011397, 10030841, 10016442, 10016443, 10016548, 10016445,
            10016447, 10016448, 10016546, 10016449
        )
        val result = repository.findByActiveTrueAndParentCategoryIsNullAndIdNotIn(excludedIds)

        return result.map {
            val children = if(it.childCategories.isEmpty()){
                emptyList<NaverShoppingCategoryDto>()
            } else {
                var dtoChildren: List<NaverShoppingCategoryDto> = emptyList()
                it.childCategories.forEach {child ->
                    if(child.childCategories.isEmpty()){
                        dtoChildren = dtoChildren + NaverShoppingCategoryDto(child.id, child.name, children = null)
                    }
                }
            }
        }
    }
}