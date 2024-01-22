package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.model.product.Product
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
class ProductController(
    @Autowired
    val productService: ProductService
) {

    @GetMapping("/shopping-category/{id}")
    fun getByCategoryIds(
        @PathVariable id: Int,
        @RequestParam page: Int = 1,
        @RequestParam numInPage: Int = 10
    ): ResponseEntity<CustomResponse<MutableList<Product>>> {
        return ResponseEntity.ok(
            CustomResponse(
                200, productService.getListByCategoryId(id, page, numInPage).content, ""
            )
        )
    }
}