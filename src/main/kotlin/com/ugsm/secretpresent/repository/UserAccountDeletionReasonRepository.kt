package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.UserAccountDeletionReason
import org.springframework.data.jpa.repository.JpaRepository

interface UserAccountDeletionReasonRepository:JpaRepository<UserAccountDeletionReason, Long> {

}