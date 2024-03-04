package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.UserDibsProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDibsProductRepository: JpaRepository<UserDibsProduct, Int> {
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<UserDibsProduct>
    fun findByUserIdOrderByProductPriceDesc(userId:Long): List<UserDibsProduct>
    fun findByUserIdOrderByProductPriceAsc(userId:Long): List<UserDibsProduct>
    fun findByUserIdAndProductId(userId: Long, productId: Long): UserDibsProduct?

}
