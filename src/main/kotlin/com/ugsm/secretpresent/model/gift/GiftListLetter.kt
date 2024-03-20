package com.ugsm.secretpresent.model.gift

import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.User
import jakarta.persistence.*

@Entity
class GiftListLetter(
    @ManyToOne
    @JoinColumn(name="gift_list_id")
    var giftList: GiftList,

    @ManyToOne
    @JoinColumn(name="gift_list_product_category_id")
    var  giftListProductCategory: GiftListProductCategory,

    @Column
    var  productCategoryName: String,

    @Column
    var imageUrl: String,

    @ManyToOne
    @JoinColumn(name="gift_list_product_id")
    var giftListProduct:GiftListProduct,

    @Column
    var productId: Long,
    @Column
    var productName: String,
    @Column
    var productPrice: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="giver_id")
    var giver:User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiver_id")
    var receiver: User,

    @Column
    var message: String,

    @Enumerated(EnumType.STRING)
    @Column
    var confirmedStatus: GiftConfirmedStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
): BaseTimeEntity()
