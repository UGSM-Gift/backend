package com.ugsm.secretpresent.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.ugsm.secretpresent.dto.LeafCategoryDto
import com.ugsm.secretpresent.model.NaverShoppingCategory
import com.ugsm.secretpresent.model.QNaverShoppingCategory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NaverShoppingCategoryRepositorySupport(
    private val queryFactory: JPAQueryFactory,
):QuerydslRepositorySupport(NaverShoppingCategory::class.java) {
    fun getAll(): List<NaverShoppingCategory> {
        val root = QNaverShoppingCategory.naverShoppingCategory
        return queryFactory
            .selectFrom(root)
            .leftJoin(root.childCategories).fetchJoin()
            .where(root.parentCategory.isNull)
            .fetch()
    }

    fun getAllLeaves():List<LeafCategoryDto>{
        val category = QNaverShoppingCategory.naverShoppingCategory

        val subQuery = queryFactory.select(category.parentCategory.id)
            .from(category)
            .where(category.parentCategory.isNotNull)

        return queryFactory
            .select(
                Projections.fields(
                    LeafCategoryDto::class.java,
                    category.id,
                    category.parentCategory.name.concat(" > ").concat(category.name).`as`("name")
                )
            )
            .from(category)
            .leftJoin(category.parentCategory)
            .where(category.id.notIn(subQuery))
            .fetch()
    }
}