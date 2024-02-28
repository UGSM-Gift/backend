package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.NaverShoppingCategoryDto
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.NaverShoppingCategoryService
import com.ugsm.secretpresent.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/product-category")
class ProductCategoryController(
    @Autowired
    val productService: ProductService,

    @Autowired
    val naverShoppingCategoryService: NaverShoppingCategoryService
) {

    @GetMapping("")
    fun getAll(): ResponseEntity<CustomResponse<List<NaverShoppingCategoryDto>>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                naverShoppingCategoryService.getAllCategories(),
                ""
            )
        )
    }
}