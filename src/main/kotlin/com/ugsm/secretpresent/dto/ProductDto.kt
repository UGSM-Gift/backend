package com.ugsm.secretpresent.dto

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.ugsm.secretpresent.enums.Marketplace
import com.ugsm.secretpresent.model.product.ProductCategory
import jakarta.persistence.*

data class ProductDto(
    var id: Long,

    var name: String = "",

    var price: Int = 0,

    var thumbnailImgUrl: String? = null,

    var brandName: String? = null,

    var buyingUrl: String,

    var freeShipping: Boolean,

    var isSoldOut: Boolean = false,
)