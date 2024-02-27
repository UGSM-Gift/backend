package com.ugsm.secretpresent.response

data class CustomResponse<T>(
    val code: Int,
    val data: T,
    val message:String,
)