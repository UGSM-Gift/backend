package com.ugsm.secretpresent.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import com.ugsm.secretpresent.dto.ImageUploadResponseDto
import com.ugsm.secretpresent.enums.S3ImageUploadType
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class AwsS3Service(
    @Autowired
    private val s3Client: S3Client
) {

    companion object {
        private const val BASE_URL = "https://cloudfront.ugsm.co.kr"
    }

    fun upload(file: MultipartFile, userId: Long, type: S3ImageUploadType): ImageUploadResponseDto = runBlocking {
        if(file.contentType == null || file.contentType!!.split("/")[0].lowercase() != "image"){
            throw IllegalArgumentException("Not supported file type")
        }

        val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename

        val byteStream = ByteStream.fromBytes(file.bytes)

        val s3Key = when (type) {
            S3ImageUploadType.PROFILE, S3ImageUploadType.GIFT_PACKAGING -> "user/${userId}/${type.dir}/${fileName}"
        }

        val req = PutObjectRequest.invoke{
            contentType=file.contentType
            contentLength=byteStream.contentLength
            bucket = "ugsm"
            key=s3Key
            body= byteStream
        }
        s3Client.putObject(req)

        return@runBlocking ImageUploadResponseDto(
            fileName = fileName,
            imageUrl = "${BASE_URL}/${s3Key}"
        )
    }
}