package com.ugsm.secretpresent.model.product

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.ugsm.secretpresent.enums.Marketplace
import com.ugsm.secretpresent.model.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,

    @Column var name: String,

    @Column var price: Int,

    @Column var thumbnailImgUrl: String,

    @Column var brandName: String,

    @Column var buyingUrl: String,

    @Enumerated(EnumType.STRING) @Column var marketplace: Marketplace,

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    var productCategories: MutableList<ProductCategory>,

    @Column var marketplaceProductId: Long,

    @Column var reviewCount: Int,

    @Column var likeCount: Int,

    @Column var overallRate: Int,

    @Column var freeShipping: Boolean,

    @Column var isSoldOut: Boolean = false,
) : BaseTimeEntity()