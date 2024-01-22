package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.product.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository:JpaRepository<Product, Int> {
    fun findByCategoriesShoppingCategoryIdIn(ids:List<Int>): List<Product>
    fun findSliceByCategoriesShoppingCategoryId(id: Int, pageable: Pageable):Slice<Product>
}