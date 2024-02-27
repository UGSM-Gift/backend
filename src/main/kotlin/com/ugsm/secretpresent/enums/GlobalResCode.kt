package com.ugsm.secretpresent.enums

enum class GlobalResCode(val code: Int) {
    OK(GlobalResCode.OK.code),
    ACCESS_TOKEN_NOT_FOUND(40001),
    INVALID_AUTH_HEADER(40002),
    NOT_ACCESS_TOKEN(40003),
    BAD_REQUEST(50000)
}