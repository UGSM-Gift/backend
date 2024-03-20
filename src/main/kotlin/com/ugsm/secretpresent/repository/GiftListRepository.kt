package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.gift.GiftList
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository
interface GiftListRepository:JpaRepository<GiftList, Int> {
    fun findByTakerId(userId: Long): List<GiftList>
    fun findByExpiredAtBetween(date1: LocalDateTime, date2: LocalDateTime): List<GiftList>
    fun findSliceByTakerIdAndAvailableAtLessThanAndExpiredAtGreaterThan(takerId: Long, pageRequest: PageRequest, date1: LocalDateTime, date2: LocalDateTime): List<GiftList>

}
