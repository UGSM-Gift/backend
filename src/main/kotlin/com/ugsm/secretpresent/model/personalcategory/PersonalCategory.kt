package com.ugsm.secretpresent.model.personalcategory

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.ugsm.secretpresent.enums.PersonalCategoryType
import jakarta.persistence.*

@Entity
class PersonalCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @Column
    var name: String,

    @Column
    @Enumerated(EnumType.STRING)
    var type: PersonalCategoryType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    var parentPersonalCategory: PersonalCategory? = null,

    @OneToMany(mappedBy = "parentPersonalCategory", cascade = [CascadeType.ALL])
    @JsonManagedReference
    var childPersonalCategories: MutableList<PersonalCategory>,

    @Column
    var question: String,

    @Column
    var viewOrder: Int,
)