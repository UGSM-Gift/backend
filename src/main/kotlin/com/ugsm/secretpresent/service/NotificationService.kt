package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.repository.EmitterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException


@Service
class NotificationService(
    @Autowired
    val emitterRepository: EmitterRepository
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

        sendToClient(userId, "EventStream Created. [userId=$userId]")
        return emitter
    }

    /**
     * 서버의 이벤트를 클라이언트에게 보내는 메서드
     * 다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다.
     *
     * @param userId - 메세지를 전송할 사용자의 아이디.
     * @param event  - 전송할 이벤트 객체.
     */
    fun notify(userId: Long, event: Any) {
        sendToClient(userId, event)
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
}