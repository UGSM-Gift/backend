package com.ugsm.secretpresent.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class NotExpiredGiftListDto {
    var listId: Int? = null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var createdAt: LocalDateTime? = null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var expiredAt: LocalDateTime? = null
    var anniversaryTitle: String = ""
    var selectedProductsNumber: Long = 0
    var receivedProductsNumber: Long = 0
}
