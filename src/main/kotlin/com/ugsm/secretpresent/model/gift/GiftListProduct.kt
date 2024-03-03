package com.ugsm.secretpresent.model.gift

import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.NaverShoppingCategory
import com.ugsm.secretpresent.model.product.Product
import jakarta.persistence.*

@Entity
class GiftListProduct(
    @ManyToOne
    @JoinColumn(name="gift_list_id")
    var giftList: GiftList,

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    var productCategory: NaverShoppingCategory?=null,

    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
) : BaseTimeEntity()
