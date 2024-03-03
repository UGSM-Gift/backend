package com.ugsm.secretpresent.dto.giftlist

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class GiftListDto(
    var listId: Int?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var createdAt: LocalDateTime?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var availableAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var expiredAt: LocalDateTime,
    var imageUrl: String,
    var anniversaryTitle: String,
    var selectedProductsNumber: Int,
    var receivedProductsNumber: Int,
)
