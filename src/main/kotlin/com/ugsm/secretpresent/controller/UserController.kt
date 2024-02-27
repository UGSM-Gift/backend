package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.Exception.BadRequestException
import com.ugsm.secretpresent.dto.ChangedUserInfo
import com.ugsm.secretpresent.dto.NicknameValidationDto
import com.ugsm.secretpresent.dto.UserAccountDeletionReasonDto
import com.ugsm.secretpresent.dto.UserInfo
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.lib.PhoneNoUtils
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.AwsS3Service
import com.ugsm.secretpresent.service.UserService
import jakarta.persistence.EntityNotFoundException
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrElse

@RestController
@RequestMapping("/api/user")
class UserController(
    @Autowired
    val userService: UserService,
    @Autowired
    val awsS3Service: AwsS3Service,
) {

    @GetMapping("/me")
    fun get(@AuthenticationPrincipal userInfo: UserInfo): ResponseEntity<CustomResponse<UserInfo>> {

        val user = userService.findById(userInfo.id).getOrElse { throw EntityNotFoundException("User Not Found") }

        val info = User.toUserInfo(user)

        return ResponseEntity.ok(CustomResponse(GlobalResCode.OK.code, info, ""))
    }

    @PutMapping("/me")
    fun updatePersonalInfo(
        @AuthenticationPrincipal userInfo: UserInfo,
        @RequestBody changedUserInfo: ChangedUserInfo
    ): ResponseEntity<CustomResponse<UserInfo?>> = runBlocking {

        changedUserInfo.apply { mobile = PhoneNoUtils.remainNumberOnly(mobile) }
            .also { it ->
                val mobile = it.mobile
                if (mobile != null && !PhoneNoUtils.validateNumberOnlyFormat(mobile)) {
                    return@runBlocking ResponseEntity
                        .badRequest()
                        .body(CustomResponse(101, null, "Not valid phone number"))
                }
            }



        if (changedUserInfo.nickname != null && userService.findByNickname(changedUserInfo.nickname) != null) {
            return@runBlocking ResponseEntity
                .badRequest()
                .body(
                    CustomResponse(
                        102,
                        null,
                        "Nickname given has been already registered"
                    )
                )
        }

        if (((changedUserInfo.name != null) && (changedUserInfo.name == changedUserInfo.nickname)) || ((changedUserInfo.name != null) && changedUserInfo.name == userInfo.nickname)) {
            return@runBlocking ResponseEntity
                .badRequest()
                .body(CustomResponse(103, null, "Name given is as same as nickname"))
        }

        val updatedUserInfo = userService.updatePersonalInfo(userInfo.id, changedUserInfo)

        return@runBlocking ResponseEntity
            .ok(CustomResponse(GlobalResCode.OK.code, updatedUserInfo, "User information updated"))
    }

    @DeleteMapping("/me")
    fun deleteAccount(
        @AuthenticationPrincipal userInfo: UserInfo,
        @RequestBody dto: UserAccountDeletionReasonDto
    ): ResponseEntity<CustomResponse<Nothing?>> {

        val user = userService.findById(userInfo.id).get()
        if (user.deleted) throw BadRequestException(101, message = "User has been already signed out")

        userService.deleteAccount(userInfo.id, dto)

        return ResponseEntity
            .ok(CustomResponse(GlobalResCode.OK.code, null, "User account is deleted successfully"))
    }

    @PostMapping("/me/logout")
    fun logout(@AuthenticationPrincipal userInfo: UserInfo): ResponseEntity<CustomResponse<Nothing?>> {
        userService.logout(userInfo.id)

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                null,
                "Logout Successful"
            )
        )
    }

    @GetMapping("/check-nickname/{nickname}")
    fun checkValidNickname(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable nickname: String
    ): ResponseEntity<CustomResponse<NicknameValidationDto>> {

        val validationResult = userService.checkNicknameValid(userInfo.id, nickname)

        return ResponseEntity.ok(CustomResponse(GlobalResCode.OK.code, validationResult, ""))
    }
}