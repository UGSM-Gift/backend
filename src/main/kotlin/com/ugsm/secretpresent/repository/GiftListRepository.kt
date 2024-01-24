package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.gift.GiftList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface GiftListRepository:JpaRepository<GiftList, Int> {
    fun findByTakerId(userId: Long): List<GiftList>

}