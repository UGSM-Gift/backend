package com.ugsm.secretpresent.model.personalcategory

import com.ugsm.secretpresent.model.BaseTimeEntity
import jakarta.persistence.*

@Entity
class SurveyPersonalCategoryAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int,

    @ManyToOne
    @JoinColumn(name = "survey_id")
    var survey: UserPersonalCategorySurvey,

    @OneToOne
    @JoinColumn(name = "selected_personal_category_id")
    var selectedPersonalCategory: PersonalCategory
):BaseTimeEntity()