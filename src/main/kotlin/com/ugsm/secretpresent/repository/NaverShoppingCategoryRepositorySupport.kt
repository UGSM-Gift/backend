package com.ugsm.secretpresent.repository

import com.querydsl.jpa.impl.JPAQueryFactory
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
}