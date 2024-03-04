package com.ugsm.secretpresent.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class GiftListProductCategoryService(
    @Autowired
    val giftListLetterService: GiftListLetterService
)
