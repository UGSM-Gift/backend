package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.AnniversaryImageDto
import com.ugsm.secretpresent.enums.GlobalResCode
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.AnniversaryImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/anniversary-images")
class AnniversaryImageController(
    @Autowired
    val anniversaryImageService: AnniversaryImageService
) {

    @GetMapping("")
    fun getAll(): ResponseEntity<CustomResponse<List<AnniversaryImageDto>>> {
        return ResponseEntity.ok(
            CustomResponse(
                GlobalResCode.OK.code,
                anniversaryImageService.findAll(),
                ""
            )
        )
    }
}