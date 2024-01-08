package com.ugsm.secretpresent.model.product

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
class ProductCategory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,

    @ManyToOne
    @JoinColumn(name="product_id")
    @JsonBackReference
    var product: Product,

    @Column var name: String,


    )