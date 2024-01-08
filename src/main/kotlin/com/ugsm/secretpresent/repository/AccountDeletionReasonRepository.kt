package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.AccountDeletionReason
import org.springframework.data.jpa.repository.JpaRepository

interface AccountDeletionReasonRepository: JpaRepository<AccountDeletionReason, Int> {
}