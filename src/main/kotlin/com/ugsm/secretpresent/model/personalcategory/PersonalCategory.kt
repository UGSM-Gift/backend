package com.ugsm.secretpresent.model.personalcategory

import com.ugsm.secretpresent.enums.PersonalCategoryType
import com.ugsm.secretpresent.model.BaseTimeEntity
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

    @OneToMany(mappedBy = "category")
    val questions: List<PersonalCategoryQuestion>,

    @Column
    var hasOtherName: Boolean,

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id")
//    @JsonBackReference
//    var parentPersonalCategory: PersonalCategory? = null,
//
//    @OneToMany(mappedBy = "parentPersonalCategory", cascade = [CascadeType.ALL])
//    @JsonManagedReference
//    var childPersonalCategories: MutableList<PersonalCategory>,

    @Column
    var viewOrder: Int,
): BaseTimeEntity()