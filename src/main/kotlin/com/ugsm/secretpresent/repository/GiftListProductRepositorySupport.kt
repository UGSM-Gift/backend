package com.ugsm.secretpresent.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.model.gift.GiftList
import com.ugsm.secretpresent.model.gift.GiftListProduct
import com.ugsm.secretpresent.model.gift.QGiftListLetter
import com.ugsm.secretpresent.model.gift.QGiftListProduct
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class GiftListProductRepositorySupport(
    private val queryFactory: JPAQueryFactory,
) : QuerydslRepositorySupport(GiftList::class.java) {
    fun getByGiftListIdAndProductCategoryIdNotGiven(
        giftListId: Int,
        shoppingCategoryId: Int
    ): MutableList<GiftListProduct> {
        val giftListProduct = QGiftListProduct.giftListProduct
        val giftListLetter = QGiftListLetter.giftListLetter
        val subQuery = queryFactory.select(
            giftListLetter.productId
        )
            .from(giftListLetter)
            .where(
                giftListLetter.confirmedStatus.ne(GiftConfirmedStatus.DENIED)
                    .and(giftListLetter.giftList.id.eq(giftListId))
            )

        return queryFactory
            .selectFrom(giftListProduct)
            .leftJoin(giftListProduct.product)
            .fetchJoin()
            .where(
                giftListProduct.giftList.id.eq(giftListId)
                    .and(giftListProduct.productCategory.id.eq(shoppingCategoryId))
                    .and(giftListProduct.product.id.notIn(subQuery))
            )
            .fetch()
    }
}