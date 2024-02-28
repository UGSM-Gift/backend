package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.personalcategory.PersonalCategoryDto
import com.ugsm.secretpresent.dto.personalcategory.PersonalCategoryWithQuestionsDto
import com.ugsm.secretpresent.enums.PersonalCategoryType
import com.ugsm.secretpresent.response.CustomResponse
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
    fun getByType(@PathVariable categoryType: PersonalCategoryType): ResponseEntity<CustomResponse<List<PersonalCategoryDto>>> {
        return ResponseEntity.ok(
            CustomResponse(
                code = 200,
                data = personalCategoryService.getType(categoryType),
                message = ""
            )
        )
    }

    @GetMapping("/{categoryIds}/question")
    fun getQuestionWithChoices(@PathVariable categoryIds: List<Int>): ResponseEntity<CustomResponse<List<PersonalCategoryWithQuestionsDto>>> {
        return ResponseEntity.ok(
            CustomResponse(
                code = 200,
                data = personalCategoryService.getCategoryQuestionsWithChoices(categoryIds),
                message = "",
            )
        )
    }
}