package com.ugsm.secretpresent.Exception

class BadRequestException (val code: Int = 50000, message: String? = null, cause: Throwable? = null) : Exception(message, cause)