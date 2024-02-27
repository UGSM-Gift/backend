package com.ugsm.secretpresent.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.ugsm.secretpresent.dto.RecommendedCategoryDto
import com.ugsm.secretpresent.repository.NaverShoppingCategoryRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GptRecommendationService(
    @Autowired
    val naverShoppingCategoryRepository: NaverShoppingCategoryRepository
) {
    val client: OkHttpClient = OkHttpClient.Builder().build()

    companion object {
        const val BASE_URL = "http://test.ugsm.co.kr:8000"
    }

    fun getRecommendedCategories(surveyId: Int): List<RecommendedCategoryDto>? {
        val objectMapper = ObjectMapper()
        val req = Request.Builder()
            .url("${BASE_URL}/gpt/recommendation/${surveyId}")
            .get()
            .build()
        val res = client.newCall(req).execute()
        if (res.code != 200) {
            throw IllegalArgumentException("메세지 전송에 실패했습니다.")
        }
        val categoryIds = objectMapper.readValue(res.body.string(), object:TypeReference<List<Int>>() {})

        val categories = naverShoppingCategoryRepository.findAllById(categoryIds)
        return categories.map{RecommendedCategoryDto(it.id, it.name)}
    }
}