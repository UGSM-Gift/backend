package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.DtoWithExceptionReason
import com.ugsm.secretpresent.dto.productcategory.BaseProductCategoryDto
import com.ugsm.secretpresent.dto.productcategory.RecommendedCategoryDto
import com.ugsm.secretpresent.dto.survey.CreateSurveyDto
import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.repository.UserRepository
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.SurveyProductCategoryService
import com.ugsm.secretpresent.service.SurveyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/survey")
class SurveyController(
    @Autowired
    val surveyProductCategoryService: SurveyProductCategoryService,

    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val surveyService: SurveyService,

    @Autowired
    val mongoTemplate: MongoTemplate

) {

    @PostMapping("")
    fun createSurvey(
        @AuthenticationPrincipal userInfo: UserInfo,
        @RequestBody dto: CreateSurveyDto,
    ): ResponseEntity<CustomResponse<Int?>> {

        val surveyId: Int

        try {
            surveyId = surveyService.create(userInfo.id, dto)
        } catch (e: Exception) {
            val dtoWithExceptionReason = DtoWithExceptionReason(
                dto, e.message ?: ""
            )
            mongoTemplate.insert(dtoWithExceptionReason, "survey-create-failed") // "user_activities" is collection name
            throw e
        }

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code, surveyId, ""
            )
        )
    }

    @GetMapping("/{surveyId}/product-category/recommended")
    fun getRecommendedProductCategories(@PathVariable surveyId: Int): ResponseEntity<CustomResponse<List<RecommendedCategoryDto>?>> {
        val prevResult = surveyProductCategoryService.getBySurveyId(surveyId)

        val result = prevResult.ifEmpty {
            surveyProductCategoryService.getRecommendedCategories(surveyId)
        }

        return ResponseEntity.ok(
            CustomResponse(
                code = GlobalResCode.OK.code,
                data = result,
                message = ""
            )
        )
    }

    @PostMapping("/{surveyId}/product-category")
    fun createSurveyProductCategories(
        @PathVariable surveyId: Int,
        @RequestBody productCategoryIds: List<Int>
    ): ResponseEntity<CustomResponse<List<BaseProductCategoryDto>>> {

        val result = surveyProductCategoryService.create(productCategoryIds, surveyId)

        return ResponseEntity.ok(
            CustomResponse(
                code = GlobalResCode.OK.code,
                data = result,
                message = ""
            )
        )
    }
}