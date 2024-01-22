package com.ugsm.secretpresent.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ugsm.secretpresent.dto.Message
import com.ugsm.secretpresent.dto.SendMessageRequest
import com.ugsm.secretpresent.lib.CoolSMSAuthenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MessageService {
    private var baseUrl = "https://api.coolsms.co.kr/messages/v4/send"
    @Value("\${coolsms.access_key}")
    private var apiKey:String?= null
    @Value("\${coolsms.access_secret}")
    private var apiSecret: String?= null

    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val authInfo = CoolSMSAuthenticator(apiKey, apiSecret).generateAuthInfo()
        val request: Request = chain.request().newBuilder().addHeader("Authorization", authInfo).build()
        chain.proceed(request)
    }.build()

    fun sendOne(message:Message){
        val objectMapper = ObjectMapper()

        val req = Request.Builder()
            .url(baseUrl)
            .post(
                objectMapper
                    .writeValueAsString(SendMessageRequest(message))
                    .toRequestBody("application/json".toMediaType())
            )
            .build()
        val res = client.newCall(req).execute()
        if (res.code != 200){
            throw IllegalArgumentException("메세지 전송에 실패했습니다.")
        }
    }
}