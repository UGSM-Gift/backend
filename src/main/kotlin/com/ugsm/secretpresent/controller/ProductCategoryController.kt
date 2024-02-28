package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.NaverShoppingCategoryDto
import com.ugsm.secretpresent.service.NaverShoppingCategoryService
import com.ugsm.secretpresent.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
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
    fun getAll(): List<NaverShoppingCategoryDto> {
        return naverShoppingCategoryService.getAllCategories()


    }
}