package com.ugsm.secretpresent.advice

import com.ugsm.secretpresent.Exception.BadRequestException
import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.response.CustomResponse
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
        val body = CustomResponse(
            40400,
            null,
            e.message ?: ""
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleValidationException(e: HandlerMethodValidationException): ResponseEntity<*>{
        val body = mutableMapOf(
            "code" to 50100,
            "data" to null,
            "message" to "Invalid Input Format"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ResponseEntity<*>{
        val body = CustomResponse(
            e.code,
            null,
            e.message ?: ""
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }


    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<*> {
        val body = CustomResponse(
            e.code,
            null,
            e.message ?: ""
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body)
    }
}