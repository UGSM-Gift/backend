package com.ugsm.secretpresent.security

import com.ugsm.secretpresent.enums.OAuth2Type
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*


@Component
class JwtProvider(
    @Value("\${jwt.secret}")
    private val secretKey: String,
) {
    private val accessTokenValidTime: Long = Duration.ofMinutes(30000).toMillis() // 만료시간 30분
    private val refreshTokenValidTime: Long = Duration.ofMinutes(10).toMillis() // 만료시간 2주

    // 회원 정보 조회
    fun getUserId(token: String?): Long {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
            .get("userId", Long::class.javaObjectType)
    }

    fun getLoginType(token: String?): String {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
            .get("loginType", String::class.java)
    }

    // 토큰 유효 및 만료 확인
    fun isExpired(token: String?): Boolean {
        try{
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body
        } catch(e: ExpiredJwtException) {
            return true
        }

        return false
    }

    // refresh 토큰 확인
    fun isRefreshToken(token: String?): Boolean {
        val header = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .header

        return header["type"].toString() == "refresh"
    }

    // access 토큰 확인
    fun isAccessToken(token: String?): Boolean {
        val header = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .header

        return header["type"].toString() == "access"
    }

    // access 토큰 생성
    fun createAccessToken(userId: Long, loginType: OAuth2Type): String {
        return createJwt(userId, loginType.name, "access", accessTokenValidTime)
    }

    // refresh 토큰 생성
    fun createRefreshToken(userId: Long, loginType: OAuth2Type): String {
        return createJwt(userId, loginType.name,"refresh", refreshTokenValidTime)
    }

    private fun createJwt(userId: Long, loginType: String, type: String?, tokenValidTime: Long): String {
        val claims = Jwts.claims()
        claims["userId"] = userId
        claims["loginType"] = loginType
        return Jwts.builder()
            .setHeaderParam("type", type)
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }
}