package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByOauth2IdAndOauth2TypeAndDeletedFalse(oauth2Id: String, oauth2Type:OAuth2Type): User?
    fun findByNickname(nickname: String): User?
    fun findByIdAndDeletedFalse(userId: Long): User?
}