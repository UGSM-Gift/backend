package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.OAuthUserInfo
import com.ugsm.secretpresent.dto.UserInfoWithToken
import com.ugsm.secretpresent.enums.Gender
import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.lib.PhoneNoUtils
import com.ugsm.secretpresent.model.CustomUserPrincipal
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.repository.UserRepository
import com.ugsm.secretpresent.security.JwtProvider
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*


@Service
class OAuth2UserService(
    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val jwtProvider: JwtProvider,

    ) : DefaultOAuth2UserService() {
    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)


        // Role generate
        val authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN")

        val oAuthType = OAuth2Type.valueOf(userRequest.clientRegistration.registrationId.uppercase())
        val oAuthUserInfo = when (oAuthType) {
            OAuth2Type.NAVER -> {
                val response = oAuth2User.attributes["response"] as Map<*, *>
                val birthdate = LocalDate.parse("${response["birthyear"]}-${response["birthday"]}")
                OAuthUserInfo(
                    oAuthType = oAuthType,
                    oAuthId = response["id"].toString(),
                    nickname = response["nickname"].toString(),
                    email = response["email"].toString(),
                    gender = if (response["gender"] == "M") Gender.MALE else Gender.FEMALE,
                    mobile = PhoneNoUtils.remainNumberOnly(response["mobile"].toString()),
                    birthdate = birthdate
                )
            }

            OAuth2Type.KAKAO -> {
                val attrs = oAuth2User.attributes as Map<String, *>
                OAuthUserInfo(
                    oAuthType = oAuthType,
                    oAuthId = attrs["id"].toString(),
                    nickname = (attrs["properties"] as Map<*, *>)["nickname"].toString(),
                    email = (attrs["kakao_account"] as Map<*, *>)["email"].toString()
                )
            }

            OAuth2Type.GOOGLE -> {
                val attrs = oAuth2User.attributes as Map<String, *>
                OAuthUserInfo(
                    oAuthType = oAuthType,
                    oAuthId = attrs["sub"].toString(),
                    nickname = attrs["name"].toString(),
                    profileImgUrl = attrs["picture"].toString(),
                    email = attrs["email"].toString()
                )
            }
        }

        val userNameAttributeName = userRequest.clientRegistration
            .providerDetails
            .userInfoEndpoint
            .userNameAttributeName

        val newUser = User.fromOAuthUserInfo(oAuthUserInfo)

        var registeredUser = userRepository.findByOauth2IdAndOauth2TypeAndDeletedFalse(oAuthUserInfo.oAuthId, oAuthType)
        if (registeredUser == null) {
            userRepository.save(newUser)
            registeredUser = newUser
        }

        val userId = registeredUser.id ?: throw EntityNotFoundException()

        val accessToken = jwtProvider.createAccessToken(userId, oAuthType)
        val refreshToken = jwtProvider.createRefreshToken(userId, oAuthType)

        registeredUser.refreshToken = refreshToken
        userRepository.save(registeredUser)

        val userInfo = UserInfoWithToken(
            id = registeredUser.id,
            birthdate = registeredUser.birthdate,
            nickname = registeredUser.nickname,
            gender = registeredUser.gender,
            mobile = registeredUser.mobile,
            name = registeredUser.name,
            profileImageUrl = registeredUser.profileImgUrl,
            accessToken = accessToken,
            refreshToken = refreshToken
        )

        return CustomUserPrincipal(userInfo, oAuth2User.attributes, authorities, userNameAttributeName)
    }
}

