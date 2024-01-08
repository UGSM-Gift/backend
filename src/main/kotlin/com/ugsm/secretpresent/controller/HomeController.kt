package com.ugsm.secretpresent.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("/")
class HomeController {
    @GetMapping("")
    fun getHome():String{
        return "index.html"
    }

    @GetMapping("oauth/callback/{provider}")
    fun getOauthTemplate(@PathVariable provider: String?):String{
        if (provider == null || (provider != "kakao" && provider != "naver"))
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "unable to find resource")

        return "oauth.html"
    }
}