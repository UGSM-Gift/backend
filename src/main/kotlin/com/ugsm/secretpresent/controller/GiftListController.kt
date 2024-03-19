package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.CreateLetterDto
import com.ugsm.secretpresent.dto.GiftListInfoDto
import com.ugsm.secretpresent.dto.giftlist.CreateGiftListDto
import com.ugsm.secretpresent.dto.giftlist.GiftListDetailDto
import com.ugsm.secretpresent.dto.giftlist.GiftListDto
import com.ugsm.secretpresent.dto.giftlist.UpdateGiftListDto
import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
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
        @RequestParam page: Int = 1,
    ): ResponseEntity<CustomResponse<List<GiftListDto>>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListService.getUserGiftList(user.id, page),
                ""
            )
        )
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

    @GetMapping("/gift-list/{giftListId}/info/not-received")
    fun getAllGiftsNotReceived(@PathVariable giftListId: Int): ResponseEntity<CustomResponse<GiftListInfoDto>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListService.getInfoWithAllGiftsNotReceived(giftListId),
                ""
            )
        )
    }

    @GetMapping("/gift-list/{giftListId}/info")
    fun getGiftListInfo(@PathVariable giftListId: Int): ResponseEntity<CustomResponse<GiftListInfoDto>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListService.getInfo(giftListId),
                ""
            )
        )
    }

    @PostMapping("/gift-list/{id}/letter")
    fun createLetter(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable id: Int,
        @RequestBody letterInfo: CreateLetterDto
    ): ResponseEntity<CustomResponse<Int?>> {

        val letterId = giftListLetterService.create(userInfo.id, id, letterInfo)
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                letterId,
                ""
            )
        )
    }


    @PatchMapping("/gift-list/{giftListId}")
    fun update(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable giftListId: Int,
        @RequestBody updateDto: UpdateGiftListDto
    ): ResponseEntity<CustomResponse<GiftListDetailDto>> {

        giftListService.update(userInfo.id, giftListId, updateDto)

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                giftListService.get(giftListId),
                ""
            )
        )
    }

    @DeleteMapping("/gift-list/{giftListId}/category/{productCategoryId}/product/{productId}")
    fun deleteProduct(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable giftListId:Int,
        @PathVariable productCategoryId: Int,
        @PathVariable productId: Long
    ): ResponseEntity<CustomResponse<Nothing?>> {
        giftListService.deleteProduct(userInfo.id, giftListId, productCategoryId, productId)

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                null,
                ""
            )
        )
    }

    @PutMapping("/gift-list/{giftListId}/category/{productCategoryId}")
    fun updateProductCategoryReceiptType(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable giftListId:Int,
        @PathVariable productCategoryId: Int,
        @RequestParam receiptType: GiftCategoryReceiptType
    ): ResponseEntity<CustomResponse<Nothing?>> {
        giftListService.updateProductCategoryReceiptType(userInfo.id, giftListId, productCategoryId, receiptType)

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                null,
                ""
            )
        )
    }
}