package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.AccountDeletionReasonDto
import com.ugsm.secretpresent.repository.AccountDeletionReasonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountDeletionReasonService(
    @Autowired
    val accountDeletionReasonRepository: AccountDeletionReasonRepository
) {
    fun findAll(): List<AccountDeletionReasonDto> {
        val reasons = accountDeletionReasonRepository.findAll()
        return reasons.map { AccountDeletionReasonDto(it.id, it.name) }
    }
}
