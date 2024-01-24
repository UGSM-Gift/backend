package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.gift.GiftListCategoryProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GiftListCategoryProductRepository:JpaRepository<GiftListCategoryProduct, Int> {

}
