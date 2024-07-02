package com.ugsm.secretpresent.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.content.toByteArray
import com.ugsm.secretpresent.dto.CropDto
import com.ugsm.secretpresent.enums.S3ImageUploadType
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*
import org.springframework.mock.web.MockMultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class AwsS3ServiceTest {

    private lateinit var s3Client: S3Client
    private lateinit var awsS3Service: AwsS3Service
    private val baseUrl = "https://example.cloudfront.net"

    @BeforeEach
    fun setUp() {
        s3Client = mock<S3Client>{}
        awsS3Service = AwsS3Service(s3Client, baseUrl)
    }

    @Test
    fun `upload should return correct ImageUploadResponseDto for valid image file`(): Unit = runBlocking {
        // Given
        val file = MockMultipartFile("file", "test.jpg", "image/jpeg", ByteArray(10))
        val userId = 1L
        val type = S3ImageUploadType.PROFILE

        `when`(s3Client.putObject(any())).thenReturn(null)

        // When
        val result = awsS3Service.upload(file, userId, type, null)

        // Then
        assertNotNull(result)
        assertTrue(result.fileName.isNotBlank())
        assertTrue(result.imageUrl.startsWith(baseUrl))
        verify(s3Client).putObject(any())
    }

    @Test
    fun `upload should throw IllegalArgumentException for non-image file`() {
        // Given
        val file = MockMultipartFile("file", "test.txt", "text/plain", ByteArray(10))
        val userId = 1L
        val type = S3ImageUploadType.PROFILE

        // When & Then
        assertThrows<IllegalArgumentException> {
            runBlocking { awsS3Service.upload(file, userId, type, null) }
        }
    }

    @Test
    fun `upload should crop image correctly when CropDto is provided`(): Unit = runBlocking {
        // Given
        val imageBytes = createTestImage(100, 100)
        val file = MockMultipartFile("file", "test.png", "image/png", imageBytes)
        val userId = 1L
        val type = S3ImageUploadType.PROFILE
        val cropDto = CropDto(10, 10, 50, 50)

        `when`(s3Client.putObject(any())).thenReturn(null)

        // When
        val result = awsS3Service.upload(file, userId, type, cropDto)

        // Then
        assertNotNull(result)
        verify(s3Client).putObject(argThat { req ->
            runBlocking {
                val contentLength = req.body?.contentLength ?: 0
                val imageBytes = req.body?.toByteArray() ?: ByteArray(0)
                val image = ImageIO.read(ByteArrayInputStream(imageBytes))

                println("Content Length: $contentLength")
                println("Image dimensions: ${image.width}x${image.height}")

                contentLength in 100..200 && image.width == 50 && image.height == 50
            }
        })
    }

    @Test
    fun `upload should throw IllegalArgumentException when crop area is invalid`() {
        // Given
        val imageBytes = createTestImage(100, 100)
        val file = MockMultipartFile("file", "test.png", "image/png", imageBytes)
        val userId = 1L
        val type = S3ImageUploadType.PROFILE
        var cropDto = CropDto(90, 90, 0, 50) // This crop area extends beyond the image bounds

        // When & Then
        assertThrows<IllegalArgumentException> {
            runBlocking { awsS3Service.upload(file, userId, type, cropDto) }
        }

        cropDto = CropDto(90, 90, 50, 0) // This crop area extends beyond the image bounds

        // When & Then
        assertThrows<IllegalArgumentException> {
            runBlocking { awsS3Service.upload(file, userId, type, cropDto) }
        }
    }

    private fun createTestImage(width: Int, height: Int): ByteArray {
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = bufferedImage.createGraphics()
        graphics.fillRect(0, 0, width, height)
        graphics.dispose()

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", outputStream)
        return outputStream.toByteArray()
    }
}