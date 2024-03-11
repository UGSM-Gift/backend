package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.model.VerificationMessage
import com.ugsm.secretpresent.repository.UserRepository
import com.ugsm.secretpresent.repository.VerificationMessageRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom
import kotlin.jvm.optionals.getOrElse

@Service
class VerificationMessageService(
    @Autowired
    private val verificationMsgRepository: VerificationMessageRepository,
    @Autowired
    private val userRepository: UserRepository
) {
    fun createCode(receiverPhoneNumber: String, userId: Long): Int {
        val code = ThreadLocalRandom.current().nextInt(100000, 1000000)

        val user = userRepository.findById(userId).getOrElse { throw EntityNotFoundException("User Not Found") }

        val messageCode = VerificationMessage(code, receiverPhoneNumber, user)
        verificationMsgRepository.save(messageCode)
        return code
    }


    @Transactional
    fun confirmCode(code: Int, receiverPhoneNumber: String, userId: Long) {
        val msg = verificationMsgRepository
                    .findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(code, receiverPhoneNumber)
            ?: throw EntityNotFoundException("No verification code found for code - $code: mobile - $receiverPhoneNumber")

        if (msg.user.id != userId) {
            throw EntityNotFoundException("No verification code found for code - $code: user - $userId")
        }

        msg.isConfirmed = true
        msg.user.mobileVerified = true
        verificationMsgRepository.save(msg)
    }

    fun isCodeValid(code: Int, receiverPhoneNumber: String, userId: Long): Boolean {
        val msg = verificationMsgRepository
            .findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(code, receiverPhoneNumber)
            ?: throw EntityNotFoundException("No verification code found for $code: $receiverPhoneNumber")

        if (msg.user.id != userId) {
            throw EntityNotFoundException("No verification code found for code - $code: user - $userId")
        }

        val now = LocalDateTime.now()

        return Duration.between(msg.createdAt, now).seconds <= 60
    }
}