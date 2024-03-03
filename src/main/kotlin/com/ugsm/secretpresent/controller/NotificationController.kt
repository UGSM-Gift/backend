package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.NotificationDto
import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
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

    @PutMapping("{notificationId}/read")
    fun markAsRead(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable notificationId: Long
    ): ResponseEntity<CustomResponse<Nothing?>> {

        notificationService.markAsRead(userInfo.id, notificationId)

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                null,
                ""
            )
        )
    }

    @GetMapping("")
    fun markAsRead(
        @AuthenticationPrincipal userInfo: UserInfo,
    ): ResponseEntity<CustomResponse<List<NotificationDto>>> {
        val result = notificationService.getAllSent(userInfo.id)
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                result,
                ""
            )
        )
    }
}