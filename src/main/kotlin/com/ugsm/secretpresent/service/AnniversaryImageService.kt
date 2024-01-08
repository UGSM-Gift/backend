package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.AnniversaryImageDto
import com.ugsm.secretpresent.repository.AnniversaryImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AnniversaryImageService(
    @Autowired
    val anniversaryImageRepository: AnniversaryImageRepository
) {

    fun findAll(): List<AnniversaryImageDto> {
        val anniversaries = anniversaryImageRepository.findAll()
        return anniversaries.map { AnniversaryImageDto (it.id, it.imageUrl)}
    }
}