package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.UserAnniversary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface UserAnniversaryRepository: JpaRepository<UserAnniversary, Int> {

    fun findByUserIdAndDeletedFalse(userId: Long): List<UserAnniversary>
    fun findByUserIdAndDateBetweenAndDeletedFalse(userId:Long, startDate: LocalDate, endDate:LocalDate):List<UserAnniversary>
    fun findByDateAndDeletedFalse(date:LocalDate):List<UserAnniversary>

}