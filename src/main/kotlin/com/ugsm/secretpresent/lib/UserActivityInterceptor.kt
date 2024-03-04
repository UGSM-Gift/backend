package com.ugsm.secretpresent.lib

import com.ugsm.secretpresent.security.JwtProvider
import com.ugsm.secretpresent.service.UserActivityService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class UserActivityInterceptor(
    @Autowired val userActivityService: UserActivityService,
    @Autowired val jwtProvider: JwtProvider
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = jwtProvider.getTokenFromRequest(request) // Replace with your JWT parsing logic
        val userId = try {
            jwtProvider.getUserId(token)
        } catch (_:Exception){
            null
        }
        val endpoint = request.requestURI
        val method = request.method
        userActivityService.logActivity(userId, endpoint, method)
        return true
    }
}