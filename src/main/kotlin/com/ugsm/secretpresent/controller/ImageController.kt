package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.CropDto
import com.ugsm.secretpresent.dto.ImageUploadResponseDto
import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.enums.S3ImageUploadType
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.AwsS3Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
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
    @PostMapping("/{uploadType}")
    fun uploadImage(
        @AuthenticationPrincipal userInfo: UserInfo,
        @PathVariable uploadType: S3ImageUploadType,
        @RequestParam image: MultipartFile,
        @RequestParam x: Int = 0,
        @RequestParam y: Int = 0,
        @RequestParam width: Int?,
        @RequestParam height: Int?,
    ): ResponseEntity<CustomResponse<ImageUploadResponseDto>> {

        val croppedParams = if(!(width == null || height == null)) CropDto(x, y, width, height) else null

        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code, s3Service.upload(image, userInfo.id, uploadType, croppedParams), ""
            )
        )
    }
}