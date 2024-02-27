package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.CreateUserAnniversaryDto
import com.ugsm.secretpresent.dto.UserAnniversaryDto
import com.ugsm.secretpresent.dto.UserInfo
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.UserAnniversaryService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.YearMonth


@RestController
@RequestMapping("/api/user")
class UserAnniversaryController(
    @Autowired
    val userAnniversaryService: UserAnniversaryService
) {

    @GetMapping("/me/anniversary")
    fun getMineByYearMonth(
        @AuthenticationPrincipal userInfo: UserInfo,
        @RequestParam yearMonth: YearMonth,
    ): ResponseEntity<CustomResponse<List<UserAnniversaryDto>>> {
        val anniversariesDto = userAnniversaryService.findByYearMonth(userInfo.id,yearMonth)

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                anniversariesDto,
                ""
            )
        )
    }

    @PostMapping("/me/anniversary")
    fun createMine(
        @AuthenticationPrincipal userInfo: UserInfo,
        @RequestBody @Valid dto: CreateUserAnniversaryDto
    ): ResponseEntity<CustomResponse<Nothing?>> {
        userAnniversaryService.create(userInfo.id, dto)
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                null,
                ""
            )
        )
    }
    @DeleteMapping("/me/anniversary/{anniversaryId}")
    fun deleteMine(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable anniversaryId: Int
    ): ResponseEntity<CustomResponse<Nothing?>> {
        userAnniversaryService.delete(userInfo.id, anniversaryId)
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                null,
                ""
            )
        )
    }
}