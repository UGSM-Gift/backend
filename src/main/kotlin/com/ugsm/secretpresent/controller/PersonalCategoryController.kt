package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.PersonalCategoryDto
import com.ugsm.secretpresent.dto.PersonalCategoryWithQuestionsDto
import com.ugsm.secretpresent.enums.PersonalCategoryType
import com.ugsm.secretpresent.service.PersonalCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/personal-category")
class PersonalCategoryController(

    @Autowired
    val personalCategoryService: PersonalCategoryService

) {

    @GetMapping("/{categoryType}")
    fun getByType(@PathVariable categoryType: PersonalCategoryType): ResponseEntity<List<PersonalCategoryDto>> {
        return ResponseEntity.ok(personalCategoryService.getType(categoryType))
    }

    @GetMapping("/{categoryIds}/question")
    fun getQuestionWithChoices(@PathVariable categoryIds: List<Int>): ResponseEntity<List<PersonalCategoryWithQuestionsDto>> {
        return ResponseEntity.ok(personalCategoryService.getCategoryQuestionsWithChoices(categoryIds))
    }
}