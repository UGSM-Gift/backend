package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.UserAccountDeletionReason
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserAccountDeletionReasonRepository:JpaRepository<UserAccountDeletionReason, Long>