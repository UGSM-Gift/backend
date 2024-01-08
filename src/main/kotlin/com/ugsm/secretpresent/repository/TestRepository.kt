package com.ugsm.secretpresent.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ugsm.secretpresent.model.QUser
import org.springframework.stereotype.Repository

@Repository
class TestRepository(
    private val queryFactory: JPAQueryFactory
) {
    fun test() = queryFactory.selectFrom(QUser.user)
        .where(QUser.user.id.longValue().between(1, 20))
        .fetch()
}