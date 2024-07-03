import com.ugsm.secretpresent.dto.UserAccountDeletionReasonDto
import com.ugsm.secretpresent.dto.user.ChangedUserInfo
import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.model.AccountDeletionReason
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.repository.*
import com.ugsm.secretpresent.security.JwtProvider
import com.ugsm.secretpresent.service.UserAnniversaryService
import com.ugsm.secretpresent.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var userSurveyRepository: UserSurveyRepository

    @Mock
    private lateinit var userAccountDeletionReasonRepository: UserAccountDeletionReasonRepository

    @Mock
    private lateinit var accountDeletionReasonRepository: AccountDeletionReasonRepository

    @Mock
    private lateinit var userAnniversaryService: UserAnniversaryService

    @Mock
    private lateinit var jwtProvider: JwtProvider

    @InjectMocks
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testUpdatePersonalInfo() {
        val userId = 1L
        val user = User(
            id = userId,
            name = "Old Name",
            email = "old@email.com",
            oauth2Type = OAuth2Type.KAKAO,
            oauth2Id = "1234"
        )
        val changedUserInfo = ChangedUserInfo(name = "New Name", email = "new@email.com")

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(userRepository.save(any())).thenReturn(user)

        val result = userService.updatePersonalInfo(userId, changedUserInfo)

        assertEquals("New Name", result.name)
        assertEquals("new@email.com", result.email)
        verify(userRepository).save(user)
    }

    @Test
    fun testCheckNicknameValid() {
        val userId = 1L
        val validNickname = "ValidNick123"
        val invalidNickname = "Invalid Nick!"

        `when`(userRepository.findByNickname(validNickname)).thenReturn(null)

        val validResult = userService.checkNicknameValid(userId, validNickname)
        val invalidResult = userService.checkNicknameValid(userId, invalidNickname)

        assertTrue(validResult.valid)
        assertFalse(invalidResult.valid)
        assertEquals("INVALID_FORMAT", invalidResult.reason)
    }

    @Test
    fun testUpdateRefreshToken() {
        val userId = 1L
        val prevRefreshToken = "oldRefreshToken"
        val newAccessToken = "newAccessToken"
        val newRefreshToken = "newRefreshToken"
        val user = User(
            id = userId,
            name = "Old Name",
            email = "old@email.com",
            oauth2Type = OAuth2Type.GOOGLE,
            oauth2Id = "1234",
            refreshToken = prevRefreshToken
        )
        `when`(jwtProvider.isExpired(prevRefreshToken)).thenReturn(false)
        `when`(jwtProvider.isRefreshToken(prevRefreshToken)).thenReturn(true)
        `when`(jwtProvider.getUserId(prevRefreshToken)).thenReturn(userId)
        `when`(jwtProvider.getLoginType(prevRefreshToken)).thenReturn(OAuth2Type.GOOGLE.name)
        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(jwtProvider.createAccessToken(userId, OAuth2Type.GOOGLE)).thenReturn(newAccessToken)
        `when`(jwtProvider.createRefreshToken(userId, OAuth2Type.GOOGLE)).thenReturn(newRefreshToken)

        val result = userService.updateRefreshToken(prevRefreshToken)

        assertEquals(newAccessToken, result.accessToken)
        assertEquals(newRefreshToken, result.refreshToken)
        assertEquals(newRefreshToken, user.refreshToken)
        verify(userRepository).save(user)
    }

    @Test
    fun testDeleteAccount() {
        val userId = 1L
        val user = User(
            id = userId,
            name = "Old Name",
            email = "old@email.com",
            oauth2Type = OAuth2Type.KAKAO,
            oauth2Id = "1234",
        )
        val deletionReason = AccountDeletionReason(id = 1, name="test reason")
        val dto = UserAccountDeletionReasonDto(deletionReasonId = 1, details = "Test detailed reason")

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(accountDeletionReasonRepository.findById(1)).thenReturn(Optional.of(deletionReason))

        userService.deleteAccount(userId, dto)

        assertTrue(user.deleted)
        verify(userRepository).save(user)
        verify(userAccountDeletionReasonRepository).save(any())
    }
}