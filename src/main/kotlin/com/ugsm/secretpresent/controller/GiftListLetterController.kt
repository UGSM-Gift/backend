package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.GiftListGivenProductDto
import com.ugsm.secretpresent.dto.GiftListLetterDto
import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.GiftListLetterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/gift-list-letter")
class GiftListLetterController(
    @Autowired
    var giftListLetterService: GiftListLetterService
) {

    @GetMapping("/product/receiver/me")
    fun getMyReceivedGiftProducts(@AuthenticationPrincipal userInfo: UserInfo): ResponseEntity<CustomResponse<List<GiftListGivenProductDto>>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListLetterService.getProductsByReceiverId(userInfo.id),
                ""
            )
        )
    }

    @GetMapping("/receiver/me/{confirmedStatus}")
    fun getMyReceivedGiftLetters(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable confirmedStatus: GiftConfirmedStatus?
    ): ResponseEntity<CustomResponse<List<GiftListLetterDto>>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListLetterService.getLettersByReceiverId(userInfo.id, confirmedStatus),
                ""
            )
        )
    }


    @GetMapping("/product/giver/me")
    fun getGiftProductsFromMe(@AuthenticationPrincipal userInfo: UserInfo): ResponseEntity<CustomResponse<List<GiftListGivenProductDto>>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListLetterService.getProductsByGiverId(userInfo.id),
                ""
            )
        )
    }

    @PutMapping("/{letterId}/confirmed-status")
    fun changeConfirmedStatus(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable letterId: Int,
        @RequestBody confirmedStatus: GiftConfirmedStatus
    ): ResponseEntity<CustomResponse<Nothing?>> {

        giftListLetterService.changeConfirmedStatus(receiverId=userInfo.id, letterId = letterId, confirmedStatus)

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                null,
                ""
            )
        )
    }
}