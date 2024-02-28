package com.ugsm.secretpresent.dto.giftlist

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class GiftListDto {
    var listId: Int? = null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var createdAt: LocalDateTime? = null
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var expiredAt: LocalDateTime? = null
    var imageUrl: String = ""
    var anniversaryTitle: String = ""
    var selectedProductsNumber: Long = 0
    var receivedProductsNumber: Long = 0
}
