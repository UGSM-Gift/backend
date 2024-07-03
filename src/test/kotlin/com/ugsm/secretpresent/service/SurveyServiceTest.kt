package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.Exception.CustomException
import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.survey.PersonalCategoryQuestionWithAnswers
import com.ugsm.secretpresent.dto.survey.CreateSurveyDto
import com.ugsm.secretpresent.dto.survey.PersonalCategoryWithQuestionsAndAnswers
import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.enums.PersonalCategoryType
import com.ugsm.secretpresent.model.*
import com.ugsm.secretpresent.model.personalcategory.PersonalCategory
import com.ugsm.secretpresent.model.personalcategory.PersonalCategoryQuestion
import com.ugsm.secretpresent.model.personalcategory.PersonalCategoryQuestionChoice
import com.ugsm.secretpresent.model.survey.UserSurvey
import com.ugsm.secretpresent.repository.*
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.LocalDate
import java.util.*

class SurveyServiceTest {

    @Mock
    private lateinit var anniversaryRepository: UserAnniversaryRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var userSurveyRepository: UserSurveyRepository

    @Mock
    private lateinit var personalCategoryRepository: PersonalCategoryRepository

    @Mock
    private lateinit var surveyPersonalCategoryRepository: SurveyPersonalCategoryRepository

    @Mock
    private lateinit var personalCategoryQuestionRepository: PersonalCategoryQuestionRepository

    @Mock
    private lateinit var personalCategoryQuestionChoiceRepository: PersonalCategoryQuestionChoiceRepository

    @Mock
    private lateinit var questionAnswerRepository: SurveyPersonalCategoryQuestionAnswerRepository

    @InjectMocks
    private lateinit var surveyService: SurveyService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testCreateSurveySuccess() {
        val userId = 1L
        val anniversaryId = 1
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val anniversary = UserAnniversary(
            user = user,
            date = LocalDate.now(),
            image = AnniversaryImage(
                "123",
                1
            ),
            name = "test-anniversary"
        )
        val choices = mutableListOf<PersonalCategoryQuestionChoice>()
        val questions = mutableListOf<PersonalCategoryQuestion>()
        val category = PersonalCategory(
            id = 1,
            name = "Category 1",
            type = PersonalCategoryType.HOBBY,
            hasOtherName = false,
            viewOrder = 1,
            questions = questions
        )
        val question = PersonalCategoryQuestion(
            id = 1,
            content = "Question 1",
            hasMultipleChoices = false,
            category = category,
            choices = choices
        )
        choices.add(PersonalCategoryQuestionChoice(id = 1, content = "Choice 1", question = question))

        val dto = CreateSurveyDto(
            anniversaryId = anniversaryId,
            answeredCategories = listOf(PersonalCategoryWithQuestionsAndAnswers(1, otherName = "Other Name")),
            questionsWithAnswers = listOf(PersonalCategoryQuestionWithAnswers(1, 1, null))
        )

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(anniversaryRepository.findById(anniversaryId)).thenReturn(Optional.of(anniversary))
        `when`(userSurveyRepository.save(any())).thenAnswer {
            ((it.arguments[0]) as UserSurvey).id = 1
            it.arguments[0] as UserSurvey
        }
        `when`(personalCategoryRepository.findById(1)).thenReturn(Optional.of(category))
        `when`(personalCategoryQuestionRepository.findById(1)).thenReturn(Optional.of(question))
        `when`(personalCategoryQuestionChoiceRepository.findById(1)).thenReturn(Optional.of(choices[0]))

        val result = surveyService.create(userId, dto)

        assertEquals(1, result)
        verify(userSurveyRepository).save(any())
        verify(surveyPersonalCategoryRepository).save(any())
        verify(questionAnswerRepository).save(any())
    }

    @Test
    fun testCreateSurveyUnauthorized() {
        val otherUserId = 2L
        val anniversaryId = 1
        val user = User(id = 1, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val anniversary = UserAnniversary(
            user = user,
            date = LocalDate.now(),
            image = AnniversaryImage(
                "123",
                1
            ),
            name = "test-anniversary"
        )

        val dto = CreateSurveyDto(
            anniversaryId = anniversaryId,
            answeredCategories = emptyList(),
            questionsWithAnswers = emptyList()
        )

        `when`(userRepository.findById(otherUserId)).thenReturn(Optional.of(user))
        `when`(anniversaryRepository.findById(anniversaryId)).thenReturn(Optional.of(anniversary))

        assertThrows(UnauthorizedException::class.java) {
            surveyService.create(otherUserId, dto)
        }
    }

    @Test
    fun testCreateSurveyAnniversaryNotFound() {
        val userId = 1L
        val anniversaryId = 1
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")

        val dto = CreateSurveyDto(
            anniversaryId = anniversaryId,
            answeredCategories = emptyList(),
            questionsWithAnswers = emptyList()
        )

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(anniversaryRepository.findById(anniversaryId)).thenReturn(Optional.empty())

        assertThrows(EntityNotFoundException::class.java) {
            surveyService.create(userId, dto)
        }
    }

    @Test
    fun testCreateSurveyFailure() {
        val userId = 1L
        val anniversaryId = 1
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val anniversary = UserAnniversary(
            user = user,
            date = LocalDate.now(),
            image = AnniversaryImage(
                "123",
                1
            ),
            name = "test-anniversary"
        )
        val choices = mutableListOf<PersonalCategoryQuestionChoice>()
        val questions = mutableListOf<PersonalCategoryQuestion>()
        val category = PersonalCategory(
            id = 1,
            name = "Category 1",
            type = PersonalCategoryType.HOBBY,
            hasOtherName = false,
            viewOrder = 1,
            questions = questions
        )
        val question = PersonalCategoryQuestion(
            id = 1,
            content = "Question 1",
            hasMultipleChoices = false,
            category = category,
            choices = choices
        )
        choices.add(PersonalCategoryQuestionChoice(id = 1, content = "Choice 1", question = question))

        val survey = UserSurvey(id = null, user = user, anniversary = anniversary)

        val dto = CreateSurveyDto(
            anniversaryId = anniversaryId,
            answeredCategories = listOf(PersonalCategoryWithQuestionsAndAnswers(1, otherName = "Other Name")),
            questionsWithAnswers = listOf(PersonalCategoryQuestionWithAnswers(1, 1, null))
        )

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(anniversaryRepository.findById(anniversaryId)).thenReturn(Optional.of(anniversary))
        `when`(userSurveyRepository.save(any())).thenReturn(survey)
        `when`(personalCategoryRepository.findById(1)).thenReturn(Optional.of(category))
        `when`(personalCategoryQuestionRepository.findById(1)).thenReturn(Optional.of(question))
        `when`(personalCategoryQuestionChoiceRepository.findById(1)).thenReturn(Optional.of(choices[0]))

        assertThrows(CustomException::class.java) {
            surveyService.create(userId, dto)
        }
    }
}