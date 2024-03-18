package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.Message

interface MessageService {
    fun sendOne(message: Message)
}