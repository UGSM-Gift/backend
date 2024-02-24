package com.ugsm.secretpresent.model.personalcategory

import com.ugsm.secretpresent.model.BaseTimeEntity
import jakarta.persistence.*

@Entity
class SurveyPersonalCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int? = null,

    @ManyToOne
    @JoinColumn(name = "survey_id")
    var survey: UserSurvey,

    @ManyToOne
    @JoinColumn(name = "selected_personal_category_id")
    var selectedPersonalCategory: PersonalCategory,

    @OneToMany(mappedBy = "category")
    var answeredQuestions: List<SurveyPersonalCategoryQuestionAnswer> = emptyList(),

    var otherName: String? = null,



    ):BaseTimeEntity()