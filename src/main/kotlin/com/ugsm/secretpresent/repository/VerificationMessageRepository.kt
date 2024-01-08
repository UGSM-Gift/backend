package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.VerificationMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationMessageRepository:JpaRepository<VerificationMessage, Long>{
    fun findByCode(code: Int): VerificationMessage?
    fun findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(code: Int, receiverPhoneNumber: String): VerificationMessage?
}