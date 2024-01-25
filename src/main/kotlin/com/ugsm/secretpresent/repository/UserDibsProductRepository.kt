package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.UserDibsProduct
import org.springframework.data.jpa.repository.JpaRepository

interface UserDibsProductRepository: JpaRepository<UserDibsProduct, Int> {
    fun findByUserId(userId: Long): List<UserDibsProduct>
    fun findByUserIdAndProductId(userId: Long, productId: Long): UserDibsProduct?

}
