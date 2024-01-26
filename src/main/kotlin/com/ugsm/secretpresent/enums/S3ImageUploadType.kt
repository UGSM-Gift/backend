package com.ugsm.secretpresent.enums

import aws.smithy.kotlin.runtime.util.type
import com.ugsm.secretpresent.service.AwsS3Service

enum class S3ImageUploadType(
    val dir: String,
) {
    PROFILE("profile"),
    GIFT_PACKAGING("gift"), ;

    fun getUrl(userId: Long): String {
        val baseUrl = AwsS3Service.BASE_URL
        return when (this) {
            PROFILE, GIFT_PACKAGING -> "${baseUrl}/${this.getFullDir(userId)}"
        }
    }

    fun getFullDir(userId: Long): String {
        return when (this) {
            PROFILE, GIFT_PACKAGING -> "user/${userId}/${this.dir}"
        }
    }
}