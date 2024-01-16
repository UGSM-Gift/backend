package com.ugsm.secretpresent.model

import jakarta.persistence.*

@Entity
class NaverShoppingCategory(
    @Id
    var id: Int,
    @Column
    var name: String,
    @ManyToOne
    @JoinColumn(name="parent_category_id")
    var parentCategory: NaverShoppingCategory?= null
) {
}