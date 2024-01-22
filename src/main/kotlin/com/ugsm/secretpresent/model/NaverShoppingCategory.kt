package com.ugsm.secretpresent.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
class NaverShoppingCategory(
    @Id
    var id: Int,
    @Column
    var name: String?= null,
    @ManyToOne
    @JoinColumn(name="parent_category_id")
    @JsonManagedReference
    var parentCategory: NaverShoppingCategory?= null,

    @OneToMany(mappedBy = "parentCategory")
    @JsonBackReference
    var childCategories: MutableList<NaverShoppingCategory>?= null
)