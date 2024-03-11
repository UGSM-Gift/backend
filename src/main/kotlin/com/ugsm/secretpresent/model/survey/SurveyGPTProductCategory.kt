package com.ugsm.secretpresent.model.survey

import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.NaverShoppingCategory
import jakarta.persistence.*

@Entity
@Table(name="survey_gpt_product_category")
class SurveyGPTProductCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_category_id")
    var productCategory: NaverShoppingCategory,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="survey_id")
    var survey: UserSurvey,
):BaseTimeEntity()