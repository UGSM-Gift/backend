package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.ImageUploadResponseDto
import com.ugsm.secretpresent.dto.UserInfo
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.enums.S3ImageUploadType
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.AwsS3Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/api/image")
class ImageController(
    @Autowired
    val s3Service: AwsS3Service,
) {
    @PostMapping("")
    fun uploadImage(
        @AuthenticationPrincipal userInfo: UserInfo,
        @RequestParam type: S3ImageUploadType,
        @RequestParam image: MultipartFile
    ): ResponseEntity<CustomResponse<ImageUploadResponseDto>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code, s3Service.upload(image, userInfo.id, type), ""
            )
        )
    }
}