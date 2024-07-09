package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.enums.PersonalCategoryType
import com.ugsm.secretpresent.model.personalcategory.PersonalCategory
import com.ugsm.secretpresent.model.personalcategory.PersonalCategoryQuestion
import com.ugsm.secretpresent.model.personalcategory.PersonalCategoryQuestionChoice
import com.ugsm.secretpresent.repository.PersonalCategoryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PersonalCategoryServiceTest {

    @Mock
    private lateinit var personalCategoryRepository: PersonalCategoryRepository

    @InjectMocks
    private lateinit var personalCategoryService: PersonalCategoryService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetType() {
        val type = PersonalCategoryType.HOBBY
        val questions1 = mutableListOf<PersonalCategoryQuestion>()
        val questions2 = mutableListOf<PersonalCategoryQuestion>()
        val categories = listOf(
            PersonalCategory(
                id = 1,
                name = "Category 1",
                type = PersonalCategoryType.HOBBY,
                hasOtherName = false,
                viewOrder = 1,
                questions = questions1
            ),
            PersonalCategory(
                id = 2,
                name = "Category 2",
                type = PersonalCategoryType.HOBBY,
                hasOtherName = true,
                viewOrder = 2,
                questions = questions2
            )
        )

        val choices1 = mutableListOf<PersonalCategoryQuestionChoice>()
        val question1 = PersonalCategoryQuestion(
            id = 1,
            content = "Question 1",
            hasMultipleChoices = false,
            category = categories[0],
            choices = choices1
        )
        choices1.add(PersonalCategoryQuestionChoice(id = 1, content = "Choice 1", question = question1))
        choices1.add(PersonalCategoryQuestionChoice(id = 2, content = "Choice 2", question = question1))


        val choices2 = mutableListOf<PersonalCategoryQuestionChoice>()
        val question2 = PersonalCategoryQuestion(
            id = 2,
            content = "Question 2",
            hasMultipleChoices = false,
            category = categories[1],
            choices = choices2
        )
        choices2.add(PersonalCategoryQuestionChoice(id = 3, content = "Choice 3", question = question2))
        choices2.add(PersonalCategoryQuestionChoice(id = 4, content = "Choice 4", question = question2))

        questions1.add(question1)
        questions2.add(question2)

        `when`(personalCategoryRepository.findByTypeOrderByViewOrderAsc(type)).thenReturn(categories)

        val result = personalCategoryService.getType(type)

        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Category 1", result[0].name)
        assertEquals(false, result[0].hasOtherName)
        assertEquals(2, result[1].id)
        assertEquals("Category 2", result[1].name)
        assertEquals(true, result[1].hasOtherName)
    }

    @Test
    fun testGetCategoryQuestionsWithChoices() {
        val questions1 = mutableListOf<PersonalCategoryQuestion>()
        val questions2 = mutableListOf<PersonalCategoryQuestion>()
        val categoryIds = listOf(1, 2)
        val categories = listOf(
            PersonalCategory(
                id = 1,
                name = "Category 1",
                type = PersonalCategoryType.HOBBY,
                hasOtherName = false,
                viewOrder = 1,
                questions = questions1
            ),
            PersonalCategory(
                id = 2,
                name = "Category 2",
                type = PersonalCategoryType.HOBBY,
                hasOtherName = true,
                viewOrder = 2,
                questions = questions2
            )
        )

        val choices1 = mutableListOf<PersonalCategoryQuestionChoice>()
        val question1 = PersonalCategoryQuestion(
            id = 1,
            content = "Question 1",
            hasMultipleChoices = false,
            category = categories[0],
            choices = choices1
        )
        choices1.add(PersonalCategoryQuestionChoice(id = 1, content = "Choice 1", question = question1))
        choices1.add(PersonalCategoryQuestionChoice(id = 2, content = "Choice 2", question = question1))


        val choices2 = mutableListOf<PersonalCategoryQuestionChoice>()
        val question2 = PersonalCategoryQuestion(
            id = 2,
            content = "Question 2",
            hasMultipleChoices = false,
            category = categories[1],
            choices = choices2
        )
        choices2.add(PersonalCategoryQuestionChoice(id = 3, content = "Choice 3", question = question2))
        choices2.add(PersonalCategoryQuestionChoice(id = 4, content = "Choice 4", question = question2))

        questions1.add(question1)
        questions2.add(question2)

        `when`(personalCategoryRepository.findAllById(categoryIds)).thenReturn(categories)

        val result = personalCategoryService.getCategoryQuestionsWithChoices(categoryIds)

        assertEquals(2, result.size)
        assertEquals(1, result[0].category.id)
        assertEquals("Category 1", result[0].category.name)
        assertEquals(false, result[0].category.hasOtherName)
        assertEquals(1, result[0].questions.size)
        assertEquals(1, result[0].questions[0].id)
        assertEquals("Question 1", result[0].questions[0].content)
        assertEquals(false, result[0].questions[0].hasMultipleChoices)
        assertEquals(2, result[0].questions[0].choices.size)
        assertEquals(1, result[0].questions[0].choices[0].id)
        assertEquals("Choice 1", result[0].questions[0].choices[0].content)
        assertEquals(2, result[0].questions[0].choices[1].id)
        assertEquals("Choice 2", result[0].questions[0].choices[1].content)
    }
}