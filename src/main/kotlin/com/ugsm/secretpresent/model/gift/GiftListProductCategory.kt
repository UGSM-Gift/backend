package com.ugsm.secretpresent.model.gift

import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.NaverShoppingCategory
import jakarta.persistence.*

@Entity
class GiftListProductCategory(
    @ManyToOne
    @JoinColumn(name="gift_list_id")
    var giftList: GiftList ?= null,

    @ManyToOne
    @JoinColumn(name="shopping_category_id")
    var shoppingCategory: NaverShoppingCategory,

    @Column
    @Enumerated(EnumType.STRING)
    var receiptType: GiftCategoryReceiptType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
): BaseTimeEntity()
