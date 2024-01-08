package com.ugsm.secretpresent.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ugsm.secretpresent.dto.Message
import com.ugsm.secretpresent.dto.SendMessageRequest
import com.ugsm.secretpresent.lib.CoolSMSAuthenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service

@Service
class MessageService {
    companion object{
        private const val BASE_URL = "https://api.coolsms.co.kr/messages/v4/send"
        private const val API_KEY = "NCSYDLETHXHOK8ZI"
        private const val API_SECRET = "VGUNT1ZEKADYYSMN2SRO06LK4VRGSMOW"
    }

    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val authInfo = CoolSMSAuthenticator(API_KEY, API_SECRET).generateAuthInfo()
        val request: Request = chain.request().newBuilder().addHeader("Authorization", authInfo).build()
        chain.proceed(request)
    }.build()

    fun sendOne(message:Message){
        val objectMapper = ObjectMapper()

        val req = Request.Builder()
            .url(BASE_URL)
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