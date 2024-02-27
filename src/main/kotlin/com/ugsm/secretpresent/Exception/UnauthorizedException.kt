package com.ugsm.secretpresent.Exception

class UnauthorizedException(val code: Int = 40100, message: String? = null, cause: Throwable? = null) : Exception(message, cause)