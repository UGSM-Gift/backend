package com.ugsm.secretpresent.model.survey

import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.NaverShoppingCategory
import jakarta.persistence.*

@Entity
class SurveyProductCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name="product_category_id")
    val productCategory: NaverShoppingCategory,

    @ManyToOne
    @JoinColumn(name="survey_id")
    val survey: UserSurvey,
):BaseTimeEntity()