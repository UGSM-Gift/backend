package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.*
import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.model.UserAccountDeletionReason
import com.ugsm.secretpresent.repository.AccountDeletionReasonRepository
import com.ugsm.secretpresent.repository.UserAccountDeletionReasonRepository
import com.ugsm.secretpresent.repository.UserPersonalCategorySurveyRepository
import com.ugsm.secretpresent.repository.UserRepository
import com.ugsm.secretpresent.security.JwtProvider
import io.jsonwebtoken.ExpiredJwtException
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.InvalidParameterException
import kotlin.jvm.optionals.getOrElse

@Service
class UserService(
    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val userPersonalCategorySurveyRepository: UserPersonalCategorySurveyRepository,

    @Autowired
    val userAccountDeletionReasonRepository: UserAccountDeletionReasonRepository,

    @Autowired
    val accountDeletionReasonRepository: AccountDeletionReasonRepository,

    @Autowired
    private val jwtProvider: JwtProvider,
) {
    fun updatePersonalInfo(userId: Long, changedUserInfo: ChangedUserInfo): UserInfo {
        val user = userRepository.findById(userId).getOrElse { throw EntityNotFoundException("User Not Found") }
        user.apply {
            name = changedUserInfo.name ?: name
            gender = changedUserInfo.gender ?: gender
            email = changedUserInfo.email ?: email
            nickname = changedUserInfo.nickname ?: nickname
            profileImgUrl = changedUserInfo.profileImageUrl ?: profileImgUrl
            mobile = changedUserInfo.mobile ?: mobile
            birthdate = changedUserInfo.birthdate ?: birthdate
        }

        userRepository.save(user)

        return User.toUserInfo(user)
    }

    fun findById(userId: Long) = userRepository.findById(userId)

    fun findByNickname(nickname: String) = userRepository.findByNickname(nickname)

    fun findSurveyByUserId(userId: Long) = userPersonalCategorySurveyRepository.findByUserId(userId)
    fun checkNicknameValid(userId: Long, nickname: String): NicknameValidationDto {
        var isValid = true
        var reason: String? = null
        val registeredUser = findByNickname(nickname)
        if (registeredUser != null) {
            isValid = false
            reason = "ALREADY_REGISTERED"
        }

        val regex = Regex("^[a-zA-Z0-9가-힣]{2,16}$")
        if (!nickname.matches(regex)) {
            isValid = false
            reason = "INVALID_FORMAT"
        }



        return NicknameValidationDto(nickname, isValid, reason)
    }

    fun updateRefreshToken(prevRefreshToken: String): TokensDto {
        if(jwtProvider.isExpired(prevRefreshToken)){
            throw ExpiredJwtException(null, null, "Refresh token is Expired")
        }

        if(!jwtProvider.isRefreshToken(prevRefreshToken)){
            throw InvalidParameterException("Token provided is not a refresh token.")
        }

        val userId = jwtProvider.getUserId(prevRefreshToken)
        val loginType = jwtProvider.getLoginType(prevRefreshToken)

        val user = findById(userId).get()

        if (user.refreshToken != prevRefreshToken){
            throw IllegalArgumentException("Token provided is a valid refresh token.")
        }

        val newAccessToken = jwtProvider.createAccessToken(userId, OAuth2Type.valueOf(loginType))
        val newRefreshToken = jwtProvider.createRefreshToken(userId, OAuth2Type.valueOf(loginType))

        user.refreshToken = newRefreshToken
        userRepository.save(user)

        return TokensDto(newAccessToken, newRefreshToken)
    }

    fun logout(userId: Long) {
        val user = findById(userId).get()

        user.refreshToken = null
        userRepository.save(user)
    }

    @Transactional
    fun deleteAccount(userId: Long, dto: UserAccountDeletionReasonDto){
        val user = findById(userId).get()
        val accountDeletionReason = accountDeletionReasonRepository.findById(dto.deletionReasonId).get()
        val userAccountDeletionReason = UserAccountDeletionReason(
            user,
            accountDeletionReason,
            dto.details
        )
        user.deleted = true
        userRepository.save(user)
        userAccountDeletionReasonRepository.save(userAccountDeletionReason)
    }
}