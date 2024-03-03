package com.ugsm.secretpresent.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.ugsm.secretpresent.dto.giftlist.GiftListDto
import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.model.gift.GiftList
import com.ugsm.secretpresent.model.gift.QGiftList
import com.ugsm.secretpresent.model.gift.QGiftListLetter
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class GiftListRepositorySupport(
    private val queryFactory: JPAQueryFactory,
) : QuerydslRepositorySupport(GiftList::class.java) {
    fun getAllByUserIdNotExpired(userId: Long, pageable: Pageable): List<GiftListDto> {
        val giftListProductLetter = QGiftListLetter.giftListLetter
        val giftList = QGiftList.giftList
        return queryFactory
            .from(giftList)
            .select(Projections.fields(
                GiftListDto::class.java,
                giftList.id.`as`("listId"),
                giftList.createdAt,
                giftList.expiredAt,
                giftList.userAnniversary.name.`as`("anniversaryTitle"),
                giftList.userAnniversary.image.imageUrl,
                giftListProductLetter.count().`as`("selectedProductsNumber"),
                giftListProductLetter.confirmedStatus.eq(GiftConfirmedStatus.CONFIRMED).count().`as`("receivedProductsNumber")
                )
            )
            .leftJoin(giftList.productLetters, giftListProductLetter)
            .where(
                giftList.taker.id.eq(userId)
//              giftList.expiredAt.after(LocalDateTime.now()).and(giftList.taker.id.eq(userId))
            )
            .groupBy(giftList.id, giftList.userAnniversary.name, giftList.userAnniversary.image.imageUrl)
            .fetch()
    }
}