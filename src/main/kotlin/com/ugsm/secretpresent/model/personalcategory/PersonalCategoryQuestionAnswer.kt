//package com.ugsm.secretpresent.model.personalcategory
//
//import jakarta.persistence.*
//
////@Entity
//class PersonalCategoryQuestionAnswer(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var id:Int,
//
//    @ManyToOne
//    @JoinColumn(name="question_id")
//    var question: PersonalCategoryQuestion,
//
//    @OneToOne
//    @JoinColumn(name = "possible_personal_category_id")
//    var possiblePersonalCategory: PersonalCategory
//)