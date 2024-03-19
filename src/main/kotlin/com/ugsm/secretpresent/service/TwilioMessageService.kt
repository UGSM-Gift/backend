package com.ugsm.secretpresent.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ugsm.secretpresent.dto.Message
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class TwilioMessageService(
    @Autowired
    val objectMapper: ObjectMapper
): MessageService {
    @Value("\${twilio.account-sid}")
    private lateinit var apiId: String
    @Value("\${twilio.auth-token}")
    private lateinit var apiSecret: String

    @Value("\${twilio.from-number}")
    private lateinit var fromNumber: String

    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val request: Request = chain.request().newBuilder().addHeader("Authorization", Credentials.basic(apiId, apiSecret)).build()
        chain.proceed(request)
    }.build()

    override fun sendOne(message:Message){
        val baseUrl = "https://api.twilio.com/2010-04-01/Accounts/${apiId}/Messages.json"


        val formBody = FormBody.Builder()
            .add("From", fromNumber)
            .add("To", "+82${message.to}")
            .add("Body", message.text)
            .build()

        val req = Request.Builder()
            .url(baseUrl)
            .post(formBody)
            .build()
        val res = client.newCall(req).execute()
        if (res.code != 201){
            throw IllegalArgumentException("메세지 전송에 실패했습니다. [${res.code}] - ${res.body.string()}")
        }
    }
}