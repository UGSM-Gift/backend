package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.gift.GiftListProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GiftListProductRepository:JpaRepository<GiftListProduct, Int> {
    fun findByGiftListIdAndProductCategoryId(giftListId: Int, productCategoryId: Int): List<GiftListProduct>
    fun findByGiftListIdAndProductId(giftListId: Int, productId: Long): GiftListProduct?
    fun findByGiftListId(giftListId: Int): List<GiftListProduct>
}
