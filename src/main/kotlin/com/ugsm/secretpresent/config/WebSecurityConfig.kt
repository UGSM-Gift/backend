package com.ugsm.secretpresent.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.ugsm.secretpresent.model.CustomUserPrincipal
import com.ugsm.secretpresent.security.JwtFilter
import com.ugsm.secretpresent.service.OAuth2UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.util.UriComponentsBuilder
import java.nio.charset.StandardCharsets


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    @Autowired
    val oAuth2UserService: OAuth2UserService,
    @Autowired
    val jwtFilter: JwtFilter,
    @Autowired
    val mapper: ObjectMapper,

    @Value("\${jwt.secret}")
    private val secretKey: String,

    @Value("\${jwt.redirect-host-on-success}")
    private val redirectUri: String,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
//            .httpBasic { basic -> basic.disable() }
            .authorizeHttpRequests { authorize ->
//                    .requestMatchers("/api/verification-message").permitAll()
////                    .requestMatchers("/api/**").authenticated()
                authorize
//                    .requestMatchers("/actuator/**").hasAuthority("ADMIN")
                    .anyRequest().permitAll()
            }
            .oauth2Login { oauth2Configurer ->
                oauth2Configurer
//                    .loginPage("/login")
                    .successHandler(successHandler())
                    .failureHandler(failureHandler())
                    .authorizationEndpoint { it.baseUri("/api/login/oauth2/authorization") }
                    .redirectionEndpoint{ it.baseUri("/api/login/oauth2/code/*")}
                    .userInfoEndpoint { it.userService(oAuth2UserService) }
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtFilter, OAuth2LoginAuthenticationFilter::class.java)
            .logout { it.permitAll() }

        return http.build()
    }

    @Bean
    fun failureHandler(): AuthenticationFailureHandler {
        return AuthenticationFailureHandler { _, response, exception ->


            response.apply{
                contentType = MediaType.APPLICATION_JSON_VALUE
                characterEncoding = StandardCharsets.UTF_8.name()
                status = HttpStatus.BAD_REQUEST.value()
            }

            val writer = response.writer
            writer.write(mapper.writeValueAsString(mutableMapOf(
                "status" to HttpStatus.BAD_REQUEST.value(),
                "data" to null,
                "message" to exception.message
            )))
        }
    }

    @Bean
    fun successHandler(): AuthenticationSuccessHandler {
        return AuthenticationSuccessHandler { request, response, authentication ->
            val userPrincipal = authentication.principal as CustomUserPrincipal
            val userInfoWithToken = userPrincipal.userInfo
            val redirectStrategy = DefaultRedirectStrategy()

            val hostName = redirectUri.split(":")[0]
            val port = redirectUri.split(":").getOrNull(1)?.toInt() ?: 80


            val uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(hostName)
                .port(port)
                .path("/oauth/callback")
                .queryParam("login", "success")
                .queryParam("accessToken", userInfoWithToken.accessToken)
                .queryParam("refreshToken", userInfoWithToken.refreshToken)
                .encode()
                .build().toUriString()

            redirectStrategy.sendRedirect(request, response, uri)

//            val mapper = ObjectMapper();
//            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//
//            mapper.registerModules(JavaTimeModule())
//            response.contentType = MediaType.APPLICATION_JSON_VALUE
//            response.characterEncoding = StandardCharsets.UTF_8.name()
//            val writer = response.writer
//            writer.write(mapper.writeValueAsString(mutableMapOf(
//                    "status" to 200,
//                    "data" to userInfoWithToken,
//                    "message" to ""
//            )))
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(
            "http://localhost:3000",
            "https://www.ugsm.co.kr:3000",
            "https://www.ugsm.co.kr",
            "http://localhost:15179",
            "http://localhost:15180"
        )
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}