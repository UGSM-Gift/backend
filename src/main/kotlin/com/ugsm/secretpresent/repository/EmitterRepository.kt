package com.ugsm.secretpresent.repository

import org.springframework.stereotype.Repository
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap


@Repository
class EmitterRepository {
    // 모든 Emitters를 저장하는 ConcurrentHashMap
    private val emitters: MutableMap<Long, SseEmitter> = ConcurrentHashMap()

    /**
     * 주어진 아이디와 이미터를 저장
     *
     * @param id      - 사용자 아이디.
     * @param emitter - 이벤트 Emitter.
     */
    fun save(id: Long, emitter: SseEmitter) {
        emitters[id] = emitter
    }

    /**
     * 주어진 아이디의 Emitter를 제거
     *
     * @param id - 사용자 아이디.
     */
    fun deleteById(id: Long) {
        emitters.remove(id)
    }

    /**
     * 주어진 아이디의 Emitter를 가져옴.
     *
     * @param id - 사용자 아이디.
     * @return SseEmitter - 이벤트 Emitter.
     */
    fun get(id: Long): SseEmitter? {
        return emitters[id]
    }

    fun getAllIds(): MutableSet<Long> {
        return emitters.keys
    }
}