import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.CreateUserAnniversaryDto
import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.model.AnniversaryImage
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.model.UserAnniversary
import com.ugsm.secretpresent.repository.AnniversaryImageRepository
import com.ugsm.secretpresent.repository.UserAnniversaryRepository
import com.ugsm.secretpresent.repository.UserRepository
import com.ugsm.secretpresent.service.UserAnniversaryService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

class UserAnniversaryServiceTest {

    @Mock
    private lateinit var userAnniversaryRepository: UserAnniversaryRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var anniversaryImageRepository: AnniversaryImageRepository

    @InjectMocks
    private lateinit var service: UserAnniversaryService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `findByYearMonth should return list of UserAnniversaryDto for specific month`() {
        // Given
        val userId = 1L
        val yearMonth = YearMonth.of(2023, 7)
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")

        val image = AnniversaryImage(id = 1, imageUrl = "test.jpg")
        val anniversary = UserAnniversary(
            id = 1,
            user = user,
            image = image,
            name = "Test Anniversary",
            date = LocalDate.of(2023, 7, 15)
        )

        whenever(userAnniversaryRepository.findByUserIdAndDateBetweenAndDeletedFalse(userId, startDate, endDate))
            .thenReturn(listOf(anniversary))

        // When
        val result = service.findByYearMonth(userId, yearMonth)

        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Test Anniversary", result[0].name)
        assertEquals(LocalDate.of(2023, 7, 15), result[0].date)
        assertEquals("test.jpg", result[0].imageUrl)
    }

    @Test
    fun `findByYearMonth should return all UserAnniversaryDto when yearMonth is null`() {
        // Given
        val userId = 1L
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val image = AnniversaryImage(id = 1, imageUrl = "test.jpg")
        val anniversary = UserAnniversary(
            id = 1,
            user = user,
            image = image,
            name = "Test Anniversary",
            date = LocalDate.of(2023, 7, 15)
        )

        whenever(userAnniversaryRepository.findByUserIdAndDeletedFalse(userId))
            .thenReturn(listOf(anniversary))

        // When
        val result = service.findByYearMonth(userId, null)

        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Test Anniversary", result[0].name)
        assertEquals(LocalDate.of(2023, 7, 15), result[0].date)
        assertEquals("test.jpg", result[0].imageUrl)
    }

    @Test
    fun `create should save new UserAnniversary`() {
        // Given
        val userId = 1L
        val dto = CreateUserAnniversaryDto(
            name = "New Anniversary",
            date = LocalDate.of(2023, 8, 1),
            imageId = 1
        )

        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val image = AnniversaryImage(id = 1, imageUrl = "test.jpg")

        whenever(userRepository.findById(userId)).thenReturn(Optional.of(user))
        whenever(anniversaryImageRepository.findById(1)).thenReturn(Optional.of(image))

        // When
        service.create(userId, dto)

        // Then
        verify(userAnniversaryRepository).save(any())
    }

    @Test
    fun `delete should mark UserAnniversary as deleted`() {
        // Given
        val userId = 1L
        val anniversaryId = 1
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val anniversary = UserAnniversary(
            id = anniversaryId.toLong(),
            user = user,
            image = AnniversaryImage(id = 1, imageUrl = "test.jpg"),
            name = "Test Anniversary",
            date = LocalDate.of(2023, 7, 15)
        )

        whenever(userAnniversaryRepository.findById(anniversaryId)).thenReturn(Optional.of(anniversary))

        // When
        service.delete(userId, anniversaryId)

        // Then
        assertTrue(anniversary.deleted)
        verify(userAnniversaryRepository).save(anniversary)
    }

    @Test
    fun `delete should throw UnauthorizedException when user is not the owner`() {
        // Given
        val userId = 1L
        val anniversaryId = 1
        val otherUser = User(id = 2L, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "4567")
        val anniversary = UserAnniversary(
            id = anniversaryId.toLong(),
            user = otherUser,
            image = AnniversaryImage(id = 1, imageUrl = "test.jpg"),
            name = "Test Anniversary",
            date = LocalDate.of(2023, 7, 15)
        )

        whenever(userAnniversaryRepository.findById(anniversaryId)).thenReturn(Optional.of(anniversary))

        // When/Then
        assertThrows<UnauthorizedException> {
            service.delete(userId, anniversaryId)
        }
    }
}