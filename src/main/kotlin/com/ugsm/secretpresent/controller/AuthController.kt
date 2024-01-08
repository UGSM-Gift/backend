package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.TokensDto
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    @Autowired
    private val userService: UserService
) {

    @PostMapping("/token/{refreshToken}")
    fun getAccessToken(
        @PathVariable refreshToken: String
    ): ResponseEntity<CustomResponse<TokensDto>> {

        val tokens = userService.updateRefreshToken(refreshToken)

        return ResponseEntity.ok(
            CustomResponse(
                HttpStatus.OK.value(),
                tokens,
                "A new access token is created successfully."
            )
        )
    }
}