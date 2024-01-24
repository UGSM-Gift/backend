package com.ugsm.secretpresent.advice

import com.ugsm.secretpresent.Exception.BadRequestException
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<*> {
        val body = mutableMapOf(
            "status" to HttpStatus.NOT_FOUND.value(),
            "data" to null,
            "message" to e.message
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleValidationException(e: HandlerMethodValidationException): ResponseEntity<*>{
        val body = mutableMapOf(
            "status" to HttpStatus.BAD_REQUEST.value(),
            "data" to null,
            "message" to "Invalid Input Format"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ResponseEntity<*>{
        val body = mutableMapOf(
            "status" to HttpStatus.BAD_REQUEST.value(),
            "data" to null,
            "message" to e.message
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }
}