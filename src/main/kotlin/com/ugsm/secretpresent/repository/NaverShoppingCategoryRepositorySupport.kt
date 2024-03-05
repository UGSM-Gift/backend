package com.ugsm.secretpresent.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.ugsm.secretpresent.dto.productcategory.LeafCategoryDto
import com.ugsm.secretpresent.dto.productcategory.ProductCategoryDto
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


    fun getAllCategories(): MutableList<ProductCategoryDto> {
        val category1 = QNaverShoppingCategory.naverShoppingCategory
        val category2 = QNaverShoppingCategory("category2")
        val category3 = QNaverShoppingCategory("category3")
        val excludedIds = listOf(
            10030807, 10011397, 10030841, 10016442, 10016443, 10016548, 10016445,
            10016447, 10016448, 10016546, 10016449
        )

        return queryFactory.select(Projections.fields(
            ProductCategoryDto::class.java,
            category1.id,
            category1.name,
            category1.imageUrl,
            category3.id.coalesce(category2.id).`as`("childId"),
            category3.name.coalesce(category2.name).`as`("childName"),
            category3.imageUrl.coalesce(category2.imageUrl).`as`("childImageUrl")
        ))
        .from(category1)
            .leftJoin(category2).on(category1.id.eq(category2.parentCategory.id).and(category2.isActive.isTrue()))
            .leftJoin(category3).on(category2.id.eq(category3.parentCategory.id).and(category3.isActive.isTrue()))
        .where(
            category1.parentCategory.isNull
                .and(category1.id.notIn(excludedIds))
                .and(category1.isActive.isTrue())
        )
        .fetch()
    }
}