package com.ugsm.secretpresent.model.gift

import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.product.Product
import jakarta.persistence.*

@Entity
class GiftListProductLetter(
    @ManyToOne
    @JoinColumn(name="giver_id")
    var giftList: GiftList,

    @ManyToOne
    @JoinColumn(name="gift_category_product_id")
    var product:Product,

    @Column
    var message: String,

    @Enumerated(EnumType.STRING)
    @Column
    var confirmedStatus: GiftConfirmedStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int
): BaseTimeEntity()
