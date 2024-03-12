package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.Exception.CustomException
import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.survey.CreateSurveyDto
import com.ugsm.secretpresent.model.survey.SurveyPersonalCategory
import com.ugsm.secretpresent.model.survey.SurveyPersonalCategoryQuestionAnswer
import com.ugsm.secretpresent.model.survey.UserSurvey
import com.ugsm.secretpresent.repository.*
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class SurveyService(
    @Autowired val anniversaryRepository: UserAnniversaryRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val userSurveyRepository: UserSurveyRepository,
    @Autowired val personalCategoryRepository: PersonalCategoryRepository,
    @Autowired val surveyPersonalCategoryRepository: SurveyPersonalCategoryRepository,
    @Autowired val personalCategoryQuestionRepository: PersonalCategoryQuestionRepository,
    @Autowired val personalCategoryQuestionChoiceRepository: PersonalCategoryQuestionChoiceRepository,
    @Autowired val questionAnswerRepository: SurveyPersonalCategoryQuestionAnswerRepository
) {
    fun create(userId: Long, dto: CreateSurveyDto): Int {
        val user = userRepository.findById(userId).get()
        val anniversary = anniversaryRepository.findById(dto.anniversaryId)
            .getOrElse { throw EntityNotFoundException("존재하지 않는 기념일 정보") }
        if (anniversary.user.id != userId) throw UnauthorizedException()
        val survey = UserSurvey(
            user = user,
            anniversary = anniversary
        )
        userSurveyRepository.save(survey)

        dto.answeredCategories.map { answeredCategory ->
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
            val categoryQuestion = personalCategoryQuestionRepository.findById(question.questionId).get()
            val personalCategory = categoryQuestion.category
            if (!question.otherAnswer.isNullOrEmpty()) {
                val questionAnswer = SurveyPersonalCategoryQuestionAnswer(
                    question = categoryQuestion,
                    personalCategory = personalCategory,
                    answer = null,
                    answerContent = question.otherAnswer,
                    survey = survey
                )
                questionAnswerRepository.save(questionAnswer)
            }
            if (question.answerId != null) {
                val choice = personalCategoryQuestionChoiceRepository.findById(question.answerId).get()
                val questionAnswer = SurveyPersonalCategoryQuestionAnswer(
                    question = categoryQuestion,
                    personalCategory = personalCategory,
                    answer = choice,
                    answerContent = choice.content,
                    survey = survey
                )
                questionAnswerRepository.save(questionAnswer)
            }
        }

        return survey.id ?: throw CustomException(message="Fail to create survey")
    }
}