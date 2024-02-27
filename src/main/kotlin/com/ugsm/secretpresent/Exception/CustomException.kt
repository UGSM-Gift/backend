package com.ugsm.secretpresent.Exception

class CustomException (val code: Int = 0, message: String? = null, cause: Throwable? = null) : Exception(message, cause)