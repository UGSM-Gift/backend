package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.UserAnniversary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface UserAnniversaryRepository: JpaRepository<UserAnniversary, Int> {

    fun findByUserId(userId: Long): List<UserAnniversary>
    fun findByUserIdAndDateBetween(userId:Long, startDate: LocalDate, endDate:LocalDate):List<UserAnniversary>
}