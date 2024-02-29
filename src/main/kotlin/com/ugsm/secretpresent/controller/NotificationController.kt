package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter


@RestController
@RequestMapping("/api/notification")
class NotificationController(
    @Autowired
    val notificationService: NotificationService
) {
    @GetMapping(value = ["/connect"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun connect(@AuthenticationPrincipal userInfo: UserInfo): ResponseEntity<SseEmitter> {
        val emitter: SseEmitter = notificationService.subscribe(userInfo.id)
        return ResponseEntity.ok(emitter)
    }
}