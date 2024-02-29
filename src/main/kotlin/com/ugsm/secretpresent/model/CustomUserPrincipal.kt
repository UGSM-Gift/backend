package com.ugsm.secretpresent.model

import com.ugsm.secretpresent.dto.user.UserInfoWithToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import java.io.Serializable

class CustomUserPrincipal(
    val userInfo: UserInfoWithToken,
    private val attributes : Map<String, Any>,
    private val authorities : List<GrantedAuthority>,
    private val nameAttributeKey: String
) : UserDetails, OAuth2User, Serializable {

    override fun getName(): String = attributes[nameAttributeKey].toString()

    override fun getAttributes(): Map<String, Any> = attributes

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = ""

    override fun getUsername(): String? = userInfo.nickname

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}