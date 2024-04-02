package com.ugsm.secretpresent.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.NotificationDto
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.repository.EmitterRepository
import com.ugsm.secretpresent.repository.NotificationRepository
import com.ugsm.secretpresent.response.CustomResponse
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrElse


@Service
class NotificationService(
    @Autowired
    val emitterRepository: EmitterRepository,
    @Autowired
    val notificationRepository: NotificationRepository,
    @Autowired
    val objectMapper: ObjectMapper
) {
    companion object {
        // 기본 타임아웃 설정
        private const val DEFAULT_TIMEOUT = 60L * 1000 * 60
    }

    /**
     * 클라이언트가 구독을 위해 호출하는 메서드.
     *
     * @param userId - 구독하는 클라이언트의 사용자 아이디.
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
    fun subscribe(userId: Long): SseEmitter {
        val emitter = createEmitter(userId)
        sendOldestUnreadNotificationToUser(userId)

        return emitter
    }

    fun sendNotification(userId: Long, dto: NotificationDto?) {
        val message = if (dto == null) "알림 없음" else ""
        val code = if (dto == null) 101 else GlobalResCode.OK.code
        val jsonString = objectMapper.writeValueAsString(
            CustomResponse(code, dto, message)
        )
        sendToClient(userId, jsonString)
    }

    @Transactional
    fun markAsRead(userId: Long, notificationId: Long) {
        val notification = notificationRepository.findById(notificationId).getOrElse {
            throw EntityNotFoundException("Notification Not Found")
        }

        if (notification.user.id?.equals(userId) == false) {
            throw UnauthorizedException(message = "Notification is not belonging to the user.")
        }

        notification.read = true
    }

    /**
     * 클라이언트에게 데이터를 전송
     *
     * @param id   - 데이터를 받을 사용자의 아이디.
     * @param data - 전송할 데이터.
     */
    private fun sendToClient(id: Long, data: Any) {
        val emitter = emitterRepository.get(id)
        if (emitter != null) {
            try {
                emitter.send(
                    SseEmitter
                        .event().id(id.toString())
                        .data(data)
                )
            } catch (exception: IOException) {
                emitterRepository.deleteById(id)
                emitter.completeWithError(exception)
            }
        }
    }

    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     *
     * @param id - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    private fun createEmitter(id: Long): SseEmitter {
        val emitter = SseEmitter(DEFAULT_TIMEOUT)
        emitterRepository.save(id, emitter)

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion { emitterRepository.deleteById(id) }
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout { emitterRepository.deleteById(id) }

        return emitter
    }

    fun sendNotificationToAllEmitter() {
        val emitters = emitterRepository.getAllIds()
        emitters.forEach { sendOldestUnreadNotificationToUser(it) }
    }

    fun sendOldestUnreadNotificationToUser(userId: Long) {
        val now = LocalDateTime.now()

        val notification =
            notificationRepository.findFirstByUserIdAndDeliveredFalseAndReservedAtLessThanOrderByIdAsc(userId, now)
        val dto = notification?.let {
            NotificationDto(
                it.id,
                it.content,
                it.type,
                it.referenceId,
                it.read,
                it.reservedAt
            )
        }
        sendNotification(userId, dto)
        notification?.delivered = true
    }

    fun getAllSent(userId: Long): List<NotificationDto> {
        val now = LocalDateTime.now()
        return notificationRepository.findByUserIdAndReservedAtLessThanOrderByIdAsc(userId, now).map {
            NotificationDto(
                it.id,
                it.content,
                it.type,
                it.referenceId,
                it.read,
                it.reservedAt
            )
        }
    }
}