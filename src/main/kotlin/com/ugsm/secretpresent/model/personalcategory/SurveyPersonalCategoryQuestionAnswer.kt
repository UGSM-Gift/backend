package com.ugsm.secretpresent.model.personalcategory

import jakarta.persistence.*

@Entity
class SurveyPersonalCategoryQuestionAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name="personal_category_id")
    val personalCategory: PersonalCategory,

    @ManyToOne
    @JoinColumn(name = "question_id")
    val question: PersonalCategoryQuestion,

    @ManyToOne
    @JoinColumn(name = "choice_id")
    val answer: PersonalCategoryQuestionChoice?,

    @Column
    val answerContent: String,
)