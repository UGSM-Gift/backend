package com.ugsm.secretpresent.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/check")
class HealthCheckController(
    @Value("\${DEPLOYMENT_FLAG:gray}")
    val flag: String?
) {

    @GetMapping("")
    fun checkHealth() = ResponseEntity.ok(flag?.uppercase())
}