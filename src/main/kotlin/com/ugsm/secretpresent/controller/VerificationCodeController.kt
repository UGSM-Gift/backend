package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.Message
import com.ugsm.secretpresent.dto.UserInfo
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.MessageService
import com.ugsm.secretpresent.service.VerificationMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class VerificationCodeController(
    @Autowired
    private var verificationMessageService: VerificationMessageService,
    @Autowired
    private var messageService: MessageService,
) {
    @PostMapping("/verification-code")
    fun createMessageCode(@AuthenticationPrincipal userInfo: UserInfo,
                          @RequestParam phoneNumber: String): ResponseEntity<CustomResponse<Nothing?>>{
        val code = verificationMessageService.createCode(phoneNumber, userInfo.id)
        phoneNumber.replace("-", "")
        val message = Message(
            from = "01089628547",
            to = phoneNumber,
            text = "[은근슨물]인증번호를 입력해주세요. [$code]"
        )

        messageService.sendOne(message)

        return ResponseEntity.ok(CustomResponse(HttpStatus.OK.value(), null, "send msg successfully"))
    }

    @PutMapping("/verification-code/{code}")
    fun updateConfirmed(@AuthenticationPrincipal userInfo: UserInfo,
                        @PathVariable code: Int,
                        @RequestParam receiverPhoneNumber: String): ResponseEntity<CustomResponse<Nothing?>> {
        if(!verificationMessageService.isCodeValid(code, receiverPhoneNumber, userInfo.id)){
            return ResponseEntity
                .badRequest()
                .body(CustomResponse(HttpStatus.BAD_REQUEST.value(), null, "Not valid code"))
        }
        verificationMessageService.confirmCode(code, receiverPhoneNumber, userInfo.id)
        return ResponseEntity
                .ok(CustomResponse(HttpStatus.OK.value(), null, "verification success"))
    }
}