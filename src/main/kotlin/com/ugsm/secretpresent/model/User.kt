package com.ugsm.secretpresent.model

import com.ugsm.secretpresent.dto.user.OAuthUserInfo
import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.enums.Gender
import com.ugsm.secretpresent.enums.OAuth2Type
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "`user`")
class User(
    @Column
    var nickname: String= "",
    @Column
    var oauth2Id: String?,
    @Column
    @Enumerated(EnumType.STRING)
    var oauth2Type: OAuth2Type,
    @Column
    var name: String? = null,
    @Column
    var profileImgUrl: String? = null,
    @Column
    var deleted: Boolean = false,
    @Column
    var email: String? = null,

    @Column
    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,
    @Column
    var mobile: String? = null,
    @Column
    var birthdate: LocalDate? = null,
    @Column
    var mobileVerified: Boolean = false,

    @Column
    var refreshToken: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
):BaseTimeEntity() {
    companion object {
        fun toUserInfo(user: User): UserInfo {
            val userId = user.id ?: throw EntityNotFoundException("user id is not provided")

            return UserInfo(
                userId,
                user.name,
                user.oauth2Type.name,
                user.nickname,
                user.profileImgUrl,
                user.gender,
                user.mobile,
                user.birthdate,
                user.email,
                user.mobileVerified
            )
        }

        fun fromOAuthUserInfo(oAuthUserInfo: OAuthUserInfo): User {
            return User(
                nickname = oAuthUserInfo.nickname ?: "",
                oauth2Id = oAuthUserInfo.oAuthId,
                oauth2Type = oAuthUserInfo.oAuthType,
                email = oAuthUserInfo.email,
                profileImgUrl = oAuthUserInfo.profileImgUrl,
                mobile = oAuthUserInfo.mobile,
                gender = oAuthUserInfo.gender,
                birthdate = oAuthUserInfo.birthdate,
            )
        }
    }
}