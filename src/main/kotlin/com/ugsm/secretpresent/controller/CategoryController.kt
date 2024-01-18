package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.NaverShoppingCategoryDto
import com.ugsm.secretpresent.model.NaverShoppingCategory
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.NaverShoppingCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/category")

class CategoryController(
    @Autowired
    var naverShoppingCategoryService: NaverShoppingCategoryService,
) {

    @GetMapping("/naver")
    fun getNaverShoppingCategories(): ResponseEntity<CustomResponse<List<NaverShoppingCategoryDto>>> {
        return ResponseEntity.ok(CustomResponse(
            200, naverShoppingCategoryService.getAll(), ""
        ))
    }
}