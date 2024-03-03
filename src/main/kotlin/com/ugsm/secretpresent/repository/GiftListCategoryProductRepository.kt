package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.gift.GiftListProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GiftListCategoryProductRepository:JpaRepository<GiftListProduct, Int> {

}
