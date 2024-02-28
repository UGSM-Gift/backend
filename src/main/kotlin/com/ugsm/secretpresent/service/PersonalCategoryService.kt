package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.personalcategory.PersonalCategoryDto
import com.ugsm.secretpresent.dto.personalcategory.PersonalCategoryQuestionChoiceDto
import com.ugsm.secretpresent.dto.personalcategory.PersonalCategoryQuestionWithChoicesDto
import com.ugsm.secretpresent.dto.personalcategory.PersonalCategoryWithQuestionsDto
import com.ugsm.secretpresent.enums.PersonalCategoryType
import com.ugsm.secretpresent.repository.PersonalCategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class PersonalCategoryService(
    @Autowired
    val personalCategoryRepository: PersonalCategoryRepository
) {

    fun getType(type:PersonalCategoryType): List<PersonalCategoryDto> {
        return personalCategoryRepository
            .findByTypeOrderByViewOrderAsc(type)
            .map{ PersonalCategoryDto(it.id, it.name, it.hasOtherName) }
    }

    fun getCategoryQuestionsWithChoices(categoryIds: List<Int>): List<PersonalCategoryWithQuestionsDto> {
        return personalCategoryRepository.findAllById(categoryIds)
            .map {category->
                val questionsDto = category.questions.map{
                    val choicesDto = it.choices.map{choice -> PersonalCategoryQuestionChoiceDto(choice.id, choice.content) }
                    PersonalCategoryQuestionWithChoicesDto(it.id, it.content, it.hasMultipleChoices,choicesDto)
                }
                val categoryDto = PersonalCategoryDto(category.id, category.name, category.hasOtherName)
                PersonalCategoryWithQuestionsDto(categoryDto, questionsDto)
            }
    }
}
