package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.productcategory.BaseProductCategoryDto
import com.ugsm.secretpresent.dto.productcategory.NaverShoppingCategoryDto
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.NaverShoppingCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/category")

class GiftCategoryController(
    @Autowired
    var naverShoppingCategoryService: NaverShoppingCategoryService,
) {

    @GetMapping("/naver")
    fun getNaverShoppingCategories(): ResponseEntity<CustomResponse<List<NaverShoppingCategoryDto>>> {
        return ResponseEntity.ok(CustomResponse(
            GlobalResCode.OK.code, naverShoppingCategoryService.getAll(), ""
        ))
    }

    @GetMapping("/naver/leaves")
    fun getNaverShoppingLeafCategories(): ResponseEntity<CustomResponse<List<BaseProductCategoryDto>>> {
        val result = naverShoppingCategoryService.getAllLeaves()

        return ResponseEntity.ok(CustomResponse(
            GlobalResCode.OK.code, result, result.count().toString()
        ))
    }
}