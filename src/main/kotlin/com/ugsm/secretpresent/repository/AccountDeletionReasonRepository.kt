package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.AccountDeletionReason
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountDeletionReasonRepository: JpaRepository<AccountDeletionReason, Int>