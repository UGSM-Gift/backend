package com.ugsm.secretpresent.response

data class CustomResponse<T>(
    val status: Int,
    val data: T,
    val message:String,
)