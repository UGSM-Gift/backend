package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.model.gift.GiftListLetter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository
interface GiftListLetterRepository : JpaRepository<GiftListLetter, Int> {
    fun findByGiverId(giverId: Long): List<GiftListLetter>
    fun findByReceiverId(receiverId: Long): List<GiftListLetter>

    fun findByReceiverIdAndConfirmedStatusIs(
        receiverId: Long,
        confirmedStatus: GiftConfirmedStatus
    ): List<GiftListLetter>

    fun findByGiftListProductCategoryIdAndConfirmedStatus(categoryId: Int, confirmedStatus: GiftConfirmedStatus)
    fun findByConfirmedStatusAndCreatedAtBetween(
        notConfirmed: GiftConfirmedStatus,
        date1: LocalDateTime,
        date2: LocalDateTime
    ): List<GiftListLetter>

    fun findByGiftListIdAndConfirmedStatusNot(id: Int?, confirmed: GiftConfirmedStatus): List<GiftListLetter>
}
