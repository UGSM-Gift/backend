package com.ugsm.secretpresent.model.survey

import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.personalcategory.PersonalCategory
import com.ugsm.secretpresent.model.personalcategory.PersonalCategoryQuestion
import com.ugsm.secretpresent.model.personalcategory.PersonalCategoryQuestionChoice
import jakarta.persistence.*

@Entity
class SurveyPersonalCategoryQuestionAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name="survey_id")
    val survey: UserSurvey,

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
):BaseTimeEntity()