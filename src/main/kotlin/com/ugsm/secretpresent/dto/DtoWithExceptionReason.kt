package com.ugsm.secretpresent.dto

import java.lang.Exception

data class DtoWithExceptionReason<T>(
    val dto: T,
    val exception: String
)
