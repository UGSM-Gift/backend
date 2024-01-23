package com.ugsm.secretpresent.model.gift

import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.NaverShoppingCategory
import com.ugsm.secretpresent.model.UserAnniversary
import jakarta.persistence.*

@Entity
class GiftListCategory(
    @ManyToOne
    @JoinColumn(name="gift_list_id")
    var giftList: GiftList,

    @ManyToOne
    @JoinColumn(name="shopping_category_id")
    var shoppingCategory: NaverShoppingCategory,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int
): BaseTimeEntity()
