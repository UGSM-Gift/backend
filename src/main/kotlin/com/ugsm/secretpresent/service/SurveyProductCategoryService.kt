package com.ugsm.secretpresent.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.ugsm.secretpresent.Exception.CustomException
import com.ugsm.secretpresent.dto.productcategory.RecommendedCategoryDto
import com.ugsm.secretpresent.model.survey.SurveyGPTProductCategory
import com.ugsm.secretpresent.model.survey.SurveyProductCategory
import com.ugsm.secretpresent.repository.NaverShoppingCategoryRepository
import com.ugsm.secretpresent.repository.SurveyGPTProductCategoryRepository
import com.ugsm.secretpresent.repository.SurveyProductCategoryRepository
import com.ugsm.secretpresent.repository.UserSurveyRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class SurveyProductCategoryService(
    @Autowired val naverShoppingCategoryRepository: NaverShoppingCategoryRepository,
    @Autowired val surveyGPTProductCategoryRepository: SurveyGPTProductCategoryRepository,
    @Autowired val surveyRepository: UserSurveyRepository,
    @Autowired val surveyProductCategoryRepository: SurveyProductCategoryRepository,
    @Autowired val objectMapper: ObjectMapper
) {
    val client: OkHttpClient = OkHttpClient
        .Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    companion object {
        const val BASE_URL = "http://test.ugsm.co.kr:8000"
    }

    fun getRecommendedCategories(surveyId: Int): List<RecommendedCategoryDto>? {
        val req = Request.Builder().url("${BASE_URL}/gpt/recommendation/${surveyId}").get().build()
        val res = client.newCall(req).execute()
        if (res.code != 200) {
            throw IllegalArgumentException("메세지 전송에 실패했습니다.")
        }
        val categoryIds = objectMapper.readValue(res.body.string(), object : TypeReference<List<Int>>() {})

        val categories = naverShoppingCategoryRepository.findAllById(categoryIds)
        val survey = surveyRepository.findById(surveyId).get()
        val gptCategories = categories.map { SurveyGPTProductCategory(productCategory = it, survey = survey) }
        surveyGPTProductCategoryRepository.saveAll(gptCategories)

        return categories.map { RecommendedCategoryDto(it.id, it.name, it.imageUrl) }
    }

    fun getBySurveyId(surveyId: Int): List<RecommendedCategoryDto> {
        return surveyGPTProductCategoryRepository.findBySurveyId(surveyId).map {
            RecommendedCategoryDto(
                it.productCategory.id,
                it.productCategory.name,
                it.productCategory.imageUrl
            )
        }
    }

    fun create(productCategoryIds: List<Int>, surveyId: Int) {
        val productCategories = naverShoppingCategoryRepository.findAllById(productCategoryIds)
        val survey = surveyRepository.findById(surveyId).get()
        if (productCategories.count() !in 1..15) throw CustomException(101, "카테고리 갯수가 1~15개가 아닙니다.")
        val surveyProductCategories =
            productCategories.map { SurveyProductCategory(productCategory = it, survey = survey) }
        surveyProductCategoryRepository.saveAll(surveyProductCategories)
    }
}