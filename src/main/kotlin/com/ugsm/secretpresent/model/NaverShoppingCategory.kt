package com.ugsm.secretpresent.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
class NaverShoppingCategory(
    @Id
    var id: Int,
    @Column
    var name: String = "",
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_category_id")
    @JsonManagedReference
    var parentCategory: NaverShoppingCategory?= null,

    @Column
    var imageUrl: String?,

    @Column
    var isActive: Boolean,

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    @JsonBackReference
    val childCategories: List<NaverShoppingCategory> = emptyList()
)