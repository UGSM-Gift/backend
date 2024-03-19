package com.ugsm.secretpresent.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import com.ugsm.secretpresent.dto.CropDto
import com.ugsm.secretpresent.dto.ImageUploadResponseDto
import com.ugsm.secretpresent.enums.S3ImageUploadType
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

@Service
class AwsS3Service(
    @Autowired
    private val s3Client: S3Client,
) {

    companion object {
        const val BASE_URL = "https://cloudfront.ugsm.co.kr"
    }

    fun upload(
        file: MultipartFile, userId: Long, type: S3ImageUploadType, croppedParams: CropDto?
    ): ImageUploadResponseDto = runBlocking {
        if (file.contentType == null || file.contentType!!.split("/")[0].lowercase() != "image") {
            throw IllegalArgumentException("Not supported file type")
        }

        val fileName = UUID.randomUUID().toString()
        val ext = getFileExtension(file) ?: throw IllegalArgumentException("Not supported file type")


        val byteStream = if (croppedParams == null) {
            ByteStream.fromBytes(file.bytes)
        } else {
            val originalImage: BufferedImage = ImageIO.read(file.inputStream)
            var (x, y, width, height) = croppedParams
            x = x.coerceAtMost(originalImage.width)
            y = y.coerceAtMost(originalImage.height)
            width = width
                        .coerceAtMost(x + width)
                        .coerceAtMost(originalImage.width - x)
                        .coerceAtLeast(0)
            height = height
                        .coerceAtMost(y + height)
                        .coerceAtMost(originalImage.height - y)
                        .coerceAtLeast(0)

            if(width == 0 || height == 0){
                throw IllegalArgumentException("이미지 선택 영역의 크기가 잘못되었습니다. 이미지 너비: ${originalImage.width} 높이: ${originalImage.height} / params: $croppedParams")
            }

            // Crop the image
            val croppedImage: BufferedImage = originalImage.getSubimage(x, y, width, height)
            ByteStream.fromBytes(bufferedImageToByteArray(croppedImage, ext))
        }
        val s3Key = "${type.getFullDir(userId)}/${fileName}"


        storeContent(s3Key, byteStream, file.contentType)

        return@runBlocking ImageUploadResponseDto(
            fileName = fileName,
            imageUrl = "${BASE_URL}/${s3Key}"
        )
    }

    private suspend fun storeContent(s3Key: String, content: ByteStream, type: String?) {
        val req = PutObjectRequest.invoke {
            contentType = type
            contentLength = content.contentLength
            bucket = "ugsm"
            key = s3Key
            body = content
        }
        s3Client.putObject(req)
    }

    private fun bufferedImageToByteArray(image: BufferedImage, formatName: String): ByteArray {
        val baos = ByteArrayOutputStream()
        ImageIO.write(image, formatName, baos)
        return baos.toByteArray()
    }

    private fun getFileExtension(file: MultipartFile): String? {
        val fileName = file.originalFilename
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1)
        }
        return null
    }
}