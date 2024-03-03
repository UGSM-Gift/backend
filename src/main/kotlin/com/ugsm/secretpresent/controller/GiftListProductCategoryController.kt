package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.service.GiftListProductCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/gift-list-category")
class GiftListProductCategoryController(
    @Autowired
    val giftListProductCategoryService: GiftListProductCategoryService
) {


}