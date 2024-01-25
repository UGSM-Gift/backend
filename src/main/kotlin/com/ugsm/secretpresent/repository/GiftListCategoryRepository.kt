package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.gift.GiftListCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GiftListCategoryRepository:JpaRepository<GiftListCategory, Int> {

}
