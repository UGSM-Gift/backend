package com.ugsm.secretpresent.model.personalcategory

import com.ugsm.secretpresent.model.BaseTimeEntity
import jakarta.persistence.*

@Entity
class PersonalCategoryQuestionChoice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column
    val content: String,

    @JoinColumn(name = "question_id")
    @ManyToOne
    val question: PersonalCategoryQuestion,
):BaseTimeEntity()