package com.ugsm.secretpresent.model.product

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.ugsm.secretpresent.enums.Marketplace
import com.ugsm.secretpresent.model.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,

    @Column var name: String = "",

    @Column var price: Int = 0,

    @Column var thumbnailImgUrl: String? = null,

    @Column var brandName: String? = null,

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

    @Column var timestamp: LocalDateTime
) : BaseTimeEntity()