package com.ugsm.secretpresent.model.personalcategory

import com.ugsm.secretpresent.model.BaseTimeEntity
import jakarta.persistence.*


@Entity
class PersonalCategoryQuestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column
    val content: String,

    @JoinColumn(name = "category_id")
    @ManyToOne
    val category: PersonalCategory,

    @OneToMany(mappedBy = "question")
    val choices: List<PersonalCategoryQuestionChoice>
) :BaseTimeEntity()