package com.ugsm.secretpresent.model.gift

import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.product.Product
import jakarta.persistence.*

@Entity
class GiftListCategoryProduct(
    @ManyToOne
    @JoinColumn(name="gift_category_id")
    var giftList: GiftList,

    @ManyToOne
    @JoinColumn(name="product_id")
    var product:Product,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int
): BaseTimeEntity()
