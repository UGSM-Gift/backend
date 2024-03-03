package com.ugsm.secretpresent.enums

import com.ugsm.secretpresent.service.AwsS3Service

enum class S3ImageUploadType(
    val dir: String,
) {
    PROFILE("profile"),
    GIFT_LIST("gift-list"),
    GIFT_LIST_LETTER("gift-list-letter");

    fun getUrl(userId: Long): String {
        val baseUrl = AwsS3Service.BASE_URL
        return "${baseUrl}/${this.getFullDir(userId)}"
    }

    fun getFullDir(userId: Long): String {
        return when (this) {
            PROFILE, GIFT_LIST, GIFT_LIST_LETTER -> "user/${userId}/${this.dir}"
        }
    }
}