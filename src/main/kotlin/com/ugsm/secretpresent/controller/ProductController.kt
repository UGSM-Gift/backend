package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.model.product.Product
import com.ugsm.secretpresent.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
class ProductController(
    @Autowired
    val productRepository: ProductRepository
) {

    @GetMapping("/shopping-category/{id}")
    fun getByCategoryIds(
        @PathVariable id:Int,
        @RequestParam page:Int=1,
        @RequestParam numInPage:Int=10
    ): MutableList<Product> {
        val pageRequest = PageRequest.of(page, numInPage)

        val result = productRepository.findSliceByCategoriesShoppingCategoryId(id, pageRequest)
        return result.content
    }
}