package com.ugsm.secretpresent.model.product

import com.fasterxml.jackson.annotation.JsonBackReference
import com.ugsm.secretpresent.model.NaverShoppingCategory
import jakarta.persistence.*

@Entity
class ProductCategory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,

    @ManyToOne
    @JoinColumn(name="product_id")
    @JsonBackReference
    var product: Product,

    @ManyToOne
    @JoinColumn(name="shopping_category_id")
    var category: NaverShoppingCategory
)