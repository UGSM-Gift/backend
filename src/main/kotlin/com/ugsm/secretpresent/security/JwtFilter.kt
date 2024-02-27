package com.ugsm.secretpresent.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.ugsm.secretpresent.lib.PhoneNoUtils
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


@Component
class JwtFilter(
    @Autowired
    private val jwtProvider: JwtProvider,

    @Autowired
    private val userRepository: UserRepository
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class, AuthenticationException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        // 로그인일 경우 건너뛰기
        if (path == "/" ||
            path.startsWith("/token") ||
            path.startsWith("/oauth")||
            path.startsWith("/login") ||
            path.startsWith("/api/login") ||
            path.startsWith("/api/oauth2") ||
            path.startsWith("/api/check")) {
            filterChain.doFilter(request, response)
            return
        }

        val token:String?

        if(path.startsWith("/api/notification")){
            token = request.getParameter("accessToken") ?: return jwtExceptionHandler(response, 40001, "Token given is not a valid access token")
        } else {
            val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return jwtExceptionHandler(response, 40002, "Token given is not a valid access token")
            }

            // Token 꺼내기
            token = authorization.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        }



        if (path.startsWith("/api/auth/token")) {
            if (jwtProvider.isExpired(token)) {
                return filterChain.doFilter(request, response)
            } else {
                throw Exception("아직 만료되지 않은 토큰")
            }
        }

        // Token Expired 되었는지 여부

        if (!jwtProvider.isAccessToken(token)) {
            return jwtExceptionHandler(response, 40003, "Token given is not a valid access token")
        }

        // UserId Token에서 꺼내기
        val userId = jwtProvider.getUserId(token)

        val user = userRepository.findByIdAndDeletedFalse(userId) ?: return jwtExceptionHandler(response, 40004, "User Not Found")


        val userInfo = User.toUserInfo(user)

        // 권한 부여

        val authorities = when(PhoneNoUtils.remainNumberOnly(userInfo.mobile)) {
            "01089628547" -> listOf("ADMIN")
            else -> listOf("USER")
        }.map{role->SimpleGrantedAuthority(role)}

        val authenticationToken =
            UsernamePasswordAuthenticationToken(userInfo, null, authorities)

        // Detail을 넣어줌
        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authenticationToken
        filterChain.doFilter(request, response)
    }

    fun jwtExceptionHandler(response: HttpServletResponse, errorCode: Int, errorMsg: String) {
        response.status = errorCode
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        val json = ObjectMapper().writeValueAsString(mutableMapOf("code" to errorCode, "data" to null, "message" to errorMsg))
        response.writer.write(json)
    }
}