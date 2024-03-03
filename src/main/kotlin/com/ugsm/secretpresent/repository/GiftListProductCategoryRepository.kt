package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.gift.GiftListProductCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GiftListProductCategoryRepository:JpaRepository<GiftListProductCategory, Int> {
    fun findByGiftListId(giftListId: Int): List<GiftListProductCategory>
    fun findByGiftListIdAndShoppingCategoryId(giftListId:Int, shoppingCategoryId: Int): GiftListProductCategory?
}
