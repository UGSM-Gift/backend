package com.ugsm.secretpresent.enums

enum class S3ImageUploadType(
    val dir: String,
) {
    PROFILE("profile"),
    GIFT_LIST("gift-list"),
    GIFT_LIST_LETTER("gift-list-letter");

    fun getUrl(userId: Long): String {
        val baseUrl = "http://cloudfront.ugsm.co.kr"
        return "${baseUrl}/${this.getFullDir(userId)}"
    }

    fun getFullDir(userId: Long): String {
        return when (this) {
            PROFILE, GIFT_LIST, GIFT_LIST_LETTER -> "user/${userId}/${this.dir}"
        }
    }
}