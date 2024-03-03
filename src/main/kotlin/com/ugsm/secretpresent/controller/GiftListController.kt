package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.CreateLetterDto
import com.ugsm.secretpresent.dto.GiftProductCategoryNotReceivedDto
import com.ugsm.secretpresent.dto.giftlist.CreateGiftListDto
import com.ugsm.secretpresent.dto.giftlist.GiftListDetailDto
import com.ugsm.secretpresent.dto.giftlist.GiftListDto
import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.GiftListLetterService
import com.ugsm.secretpresent.service.GiftListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class GiftListController(
    @Autowired
    var giftListService: GiftListService,

    @Autowired
    val giftListLetterService: GiftListLetterService
) {


    @PostMapping("/gift-list")
    fun createList(
        @AuthenticationPrincipal user: UserInfo,
        @RequestBody createGiftListDto: CreateGiftListDto,
    ): ResponseEntity<CustomResponse<Int?>> {
        val giftListId = giftListService.create(user.id, createGiftListDto)

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListId,
                ""
            )
        )
    }

    @GetMapping("/user/me/gift-list")
    fun getMyList(
        @AuthenticationPrincipal user: UserInfo,
        @RequestParam p: Int = 1,
    ): List<GiftListDto> {
        return giftListService.getUserGiftList(user.id, p)
    }

    @GetMapping("/gift-list/{giftListId}")
    fun getById(@PathVariable giftListId: Int): ResponseEntity<CustomResponse<GiftListDetailDto>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListService.get(giftListId),
                ""
            )
        )
    }

    @GetMapping("/gift-list/{giftListId}/gift/not-received")
    fun getAllGiftsNotReceived(@PathVariable giftListId: Int): ResponseEntity<CustomResponse<GiftProductCategoryNotReceivedDto>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListService.getAllGiftsNotReceived(giftListId),
                ""
            )
        )
    }

    @PostMapping("/gift-list/{id}/letter")
    fun createLetter(@AuthenticationPrincipal userInfo: UserInfo,
                     @PathVariable id:Int,
                     @RequestBody letterInfo: CreateLetterDto) {
        giftListLetterService.create(userInfo.id, id,letterInfo)
    }
}