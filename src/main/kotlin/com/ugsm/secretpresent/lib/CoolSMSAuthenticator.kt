package com.ugsm.secretpresent.lib

import org.springframework.security.crypto.codec.Hex
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class CoolSMSAuthenticator(
    private val apiKey: String,
    private val apiSecretKey: String
) {


    companion object {
        private const val ENCRYPTION_ALGORITHM = "HmacSHA256"
    }

    @Throws
    fun generateAuthInfo(): String {
        if (apiKey == "" || apiSecretKey == "") {
            throw IllegalArgumentException("유효한 key값을 넣으셔야 합니다.")
        }

        val salt = UUID.randomUUID().toString().replace(Regex("-"), "")
        val date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString().split(Regex("\\[")).toTypedArray()[0]

        val encryptionInstance = Mac.getInstance(ENCRYPTION_ALGORITHM)
        val secretKey = SecretKeySpec(apiSecretKey.toByteArray(StandardCharsets.UTF_8), ENCRYPTION_ALGORITHM)

        encryptionInstance.init(secretKey)
        val signature = String(
            (date + salt).toByteArray(StandardCharsets.UTF_8)
                .let { encryptionInstance.doFinal(it) }
                .let { Hex.encode(it) }
        )
        return "HMAC-SHA256 Apikey=${apiKey}, Date=${date}, salt=${salt}, signature=${signature}"
    }
}
