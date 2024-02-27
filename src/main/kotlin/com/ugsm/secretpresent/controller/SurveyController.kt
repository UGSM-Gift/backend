package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.CreateSurveyDto
import com.ugsm.secretpresent.dto.RecommendedCategoryDto
import com.ugsm.secretpresent.dto.UserInfo
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.model.personalcategory.SurveyPersonalCategory
import com.ugsm.secretpresent.model.personalcategory.SurveyPersonalCategoryQuestionAnswer
import com.ugsm.secretpresent.model.personalcategory.UserSurvey
import com.ugsm.secretpresent.repository.*
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.GptRecommendationService
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
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
    val gptRecommendationService: GptRecommendationService,

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
            surveyCategory
        }

        dto.questionsWithAnswers.map { question ->
            val categoryQuestion = personalCategoryQuestionRepository.findById(question.id).get()
            val personalCategory = categoryQuestion.category
            if(!question.otherAnswer.isNullOrEmpty()) {
                val questionAnswer = SurveyPersonalCategoryQuestionAnswer(
                    question = categoryQuestion,
                    personalCategory = personalCategory,
                    answer = null,
                    answerContent = question.otherAnswer,
                )
                questionAnswerRepository.save(questionAnswer)
            }
            if (question.answerId != null){
                val choice = personalCategoryQuestionChoiceRepository.findById(question.answerId).get()
                val questionAnswer = SurveyPersonalCategoryQuestionAnswer(
                    question = categoryQuestion,
                    personalCategory = personalCategory,
                    answer = choice,
                    answerContent = choice.content,
                )
                questionAnswerRepository.save(questionAnswer)
            }
        }

        return ResponseEntity.ok(
            CustomResponse(
                200, survey.id, ""
            )
        )
    }

    @GetMapping("/{surveyId}/product-category/recommended")
    fun getRecommendedProductCategories(@PathVariable surveyId: Int): ResponseEntity<CustomResponse<List<RecommendedCategoryDto>?>> {
        val result = gptRecommendationService.getRecommendedCategories(surveyId)

        return ResponseEntity.ok(
            CustomResponse(
                code=GlobalResCode.OK.code,
                data=result,
                message = ""
            )
        )
    }
}