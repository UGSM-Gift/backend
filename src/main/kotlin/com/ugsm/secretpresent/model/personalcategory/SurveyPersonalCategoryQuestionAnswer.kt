//package com.ugsm.secretpresent.model.personalcategory
//
//import com.fasterxml.jackson.annotation.JsonBackReference
//import jakarta.persistence.Entity
//import jakarta.persistence.GeneratedValue
//import jakarta.persistence.GenerationType
//import jakarta.persistence.Id
//import jakarta.persistence.JoinColumn
//import jakarta.persistence.ManyToOne
//
////@Entity
//class SurveyPersonalCategoryQuestionAnswer(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var id: Long,
//
//    @ManyToOne
//    @JoinColumn(name="survey_id")
//    @JsonBackReference
//    var userPersonalCategorySurvey: UserPersonalCategorySurvey,
//
//    @ManyToOne
//    @JoinColumn(name="question_answer_id")
//    var selectedAnswer: PersonalCategoryQuestionAnswer
//) {
//}