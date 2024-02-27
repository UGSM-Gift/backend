package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.AccountDeletionReasonDto
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.AccountDeletionReasonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/my-page")
class MyPageController(
    @Autowired
    val accountDeletionReasonService: AccountDeletionReasonService
) {
    @GetMapping("/account-deletion-reasons")
    fun getAll(): ResponseEntity<CustomResponse<List<AccountDeletionReasonDto>>>{
        val reasons = accountDeletionReasonService.findAll()

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                reasons,
                ""
            )
        )
    }
}