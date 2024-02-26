package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.CreateSurveyDto
import com.ugsm.secretpresent.dto.UserInfo
import com.ugsm.secretpresent.model.personalcategory.SurveyPersonalCategory
import com.ugsm.secretpresent.model.personalcategory.SurveyPersonalCategoryQuestionAnswer
import com.ugsm.secretpresent.model.personalcategory.UserSurvey
import com.ugsm.secretpresent.repository.*
import com.ugsm.secretpresent.response.CustomResponse
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrElse

@RestController
@RequestMapping("/api/survey")
class SurveyController(
    @Autowired
    val surveyPersonalCategoryRepository: SurveyPersonalCategoryRepository,
    @Autowired
    val questionAnswerRepository: SurveyPersonalCategoryQuestionAnswerRepository,

    @Autowired
    val userSurveyRepository: UserSurveyRepository,
    @Autowired
    val anniversaryRepository: UserAnniversaryRepository,
    @Autowired
    val personalCategoryRepository: PersonalCategoryRepository,

    @Autowired
    val personalCategoryQuestionRepository: PersonalCategoryQuestionRepository,

    @Autowired
    val personalCategoryQuestionChoiceRepository: PersonalCategoryQuestionChoiceRepository,


    @Autowired
    val userRepository: UserRepository,
) {

    @PostMapping("")
    fun createSurvey(@AuthenticationPrincipal userInfo: UserInfo, @RequestBody dto: CreateSurveyDto): ResponseEntity<CustomResponse<Int?>> {
        val user = userRepository.findById(userInfo.id).get()
        val anniversary = anniversaryRepository.findById(dto.anniversaryId).getOrElse{throw EntityNotFoundException("존재하지 않는 기념일 정보")}
        if(anniversary.user.id != userInfo.id) throw UnauthorizedException()
        val survey = UserSurvey(
            user = user,
            anniversary = anniversary
        )
        userSurveyRepository.save(survey)

        dto.answeredCategories.map{answeredCategory->
            val category = personalCategoryRepository.findById(answeredCategory.id).get()
            val surveyCategory = SurveyPersonalCategory(
                survey = survey,
                selectedPersonalCategory = category,
                otherName = answeredCategory.otherName,
                categoryName = category.name
            )
            surveyPersonalCategoryRepository.save(surveyCategory)

            answeredCategory.questionsWithAnswers?.map { question ->
                val categoryQuestion = personalCategoryQuestionRepository.findById(question.id).get()
                if(!question.otherAnswer.isNullOrEmpty()) {
                    val questionAnswer = SurveyPersonalCategoryQuestionAnswer(
                        question = categoryQuestion,
                        category = surveyCategory,
                        answer = null,
                        answerContent = question.otherAnswer,
                    )
                    questionAnswerRepository.save(questionAnswer)
                }
                if (!question.answerIds.isNullOrEmpty()){
                    val answers = question.answerIds.map {id->
                        val choice = personalCategoryQuestionChoiceRepository.findById(id).get()
                        SurveyPersonalCategoryQuestionAnswer(
                            question = categoryQuestion,
                            category = surveyCategory,
                            answer = choice,
                            answerContent = choice.content,
                        )
                    }
                    questionAnswerRepository.saveAll(answers)
                }
            }
        }

        return ResponseEntity.ok(
            CustomResponse(
                200, survey.id, ""
            )
        )
    }
}