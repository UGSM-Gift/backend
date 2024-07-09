import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.model.VerificationMessage
import com.ugsm.secretpresent.repository.UserRepository
import com.ugsm.secretpresent.repository.VerificationMessageRepository
import com.ugsm.secretpresent.service.VerificationMessageService
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VerificationMessageServiceTest {

    @Mock
    private lateinit var verificationMsgRepository: VerificationMessageRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var service: VerificationMessageService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `createCode should generate and save a new verification code`() {
        // Given
        val phoneNumber = "1234567890"
        val userId = 1L
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")

        whenever(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user))

        // When
        val result = service.createCode(phoneNumber, userId)

        // Then
        assertTrue(result in 100000..999999)
        verify(verificationMsgRepository).save(any())
    }

    @Test
    fun `createCode should throw EntityNotFoundException when user not found`() {
        // Given
        val phoneNumber = "1234567890"
        val userId = 1L

        whenever(userRepository.findById(userId)).thenReturn(java.util.Optional.empty())

        // When/Then
        assertThrows<EntityNotFoundException> {
            service.createCode(phoneNumber, userId)
        }
    }

    @Test
    fun `confirmCode should mark message as confirmed and user as verified`() {
        // Given
        val code = 123456
        val phoneNumber = "1234567890"
        val userId = 1L
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val verificationMessage = VerificationMessage(code = code, receiverPhoneNumber = phoneNumber, user = user)

        whenever(
            verificationMsgRepository.findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(
                code,
                phoneNumber
            )
        )
            .thenReturn(verificationMessage)

        // When
        service.confirmCode(code, phoneNumber, userId)

        // Then
        assertTrue(verificationMessage.isConfirmed)
        assertTrue(verificationMessage.user.mobileVerified)
        verify(verificationMsgRepository).save(verificationMessage)
    }

    @Test
    fun `confirmCode should throw EntityNotFoundException when no verification code found`() {
        // Given
        val code = 123456
        val phoneNumber = "1234567890"
        val userId = 1L

        whenever(
            verificationMsgRepository.findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(
                code,
                phoneNumber
            )
        )
            .thenReturn(null)

        // When/Then
        assertThrows<EntityNotFoundException> {
            service.confirmCode(code, phoneNumber, userId)
        }
    }

    @Test
    fun `confirmCode should throw EntityNotFoundException when user ID doesn't match`() {
        // Given
        val code = 123456
        val phoneNumber = "1234567890"
        val userId = 1L
        val user = User(id = 2L, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val verificationMessage = VerificationMessage(code = code, receiverPhoneNumber = phoneNumber, user = user)
        verificationMessage.createdAt = LocalDateTime.now().minusMinutes(-3)

        whenever(
            verificationMsgRepository.findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(
                code,
                phoneNumber
            )
        )
            .thenReturn(verificationMessage)

        // When/Then
        assertThrows<EntityNotFoundException> {
            service.confirmCode(code, phoneNumber, userId)
        }
    }

    @Test
    fun `isCodeValid should return true for valid code within time limit`() {
        // Given
        val code = 123456
        val phoneNumber = "1234567890"
        val userId = 1L
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val verificationMessage = VerificationMessage(
            code = code,
            receiverPhoneNumber = phoneNumber,
            user = user,
            isConfirmed = false,
        )
        verificationMessage.createdAt = LocalDateTime.now().minusMinutes(-3)

        whenever(
            verificationMsgRepository.findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(
                code,
                phoneNumber
            )
        )
            .thenReturn(verificationMessage)

        // When
        val result = service.isCodeValid(code, phoneNumber, userId)

        // Then
        assertTrue(result)
    }

    @Test
    fun `isCodeValid should return false for expired code`() {
        // Given
        val code = 123456
        val phoneNumber = "1234567890"
        val userId = 1L
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val verificationMessage = VerificationMessage(
            code = code,
            receiverPhoneNumber = phoneNumber,
            user = user,
        )
        verificationMessage.createdAt = LocalDateTime.now().minusMinutes(30)


        whenever(
            verificationMsgRepository.findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(
                code,
                phoneNumber
            )
        )
            .thenReturn(verificationMessage)

        // When
        val result = service.isCodeValid(code, phoneNumber, userId)

        // Then
        assertFalse(result)
    }

    @Test
    fun `isCodeValid should throw EntityNotFoundException when no verification code found`() {
        // Given
        val code = 123456
        val phoneNumber = "1234567890"
        val userId = 1L

        whenever(
            verificationMsgRepository.findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(
                code,
                phoneNumber
            )
        )
            .thenReturn(null)

        // When/Then
        assertThrows<EntityNotFoundException> {
            service.isCodeValid(code, phoneNumber, userId)
        }
    }

    @Test
    fun `isCodeValid should throw EntityNotFoundException when user ID doesn't match`() {
        // Given
        val code = 123456
        val phoneNumber = "1234567890"
        val userId = 1L
        val user = User(id = 2L, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val verificationMessage = VerificationMessage(
            code = code,
            receiverPhoneNumber = phoneNumber,
            user = user,
        )

        whenever(
            verificationMsgRepository.findFirstByCodeAndReceiverPhoneNumberAndIsConfirmedFalseOrderByIdDesc(
                eq(code),
                eq(phoneNumber)
            )
        )
            .thenReturn(verificationMessage)

        // When/Then
        assertThrows<EntityNotFoundException> {
            service.isCodeValid(code, phoneNumber, userId)
        }
    }

}