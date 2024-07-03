import com.fasterxml.jackson.databind.ObjectMapper
import com.ugsm.secretpresent.dto.NotificationDto
import com.ugsm.secretpresent.enums.NotificationType
import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.model.Notification
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.repository.EmitterRepository
import com.ugsm.secretpresent.repository.NotificationRepository
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.NotificationService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.LocalDateTime
import java.util.*

class NotificationServiceTest {

    @InjectMocks
    @Spy
    lateinit var notificationService: NotificationService

    @Mock
    lateinit var emitterRepository: EmitterRepository

    @Mock
    lateinit var notificationRepository: NotificationRepository

    @Mock
    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test subscribe`() {
        val userId: Long = 1
        doNothing().`when`(emitterRepository).save(eq(userId), any<SseEmitter>())
        doNothing().`when`(notificationService).sendOldestUnreadNotificationToUser(eq(userId))

        val result = notificationService.subscribe(userId)

        assertNotNull(result)
    }

    @Test
    fun `test sendNotification`() {
        val userId: Long = 1
        val dto: NotificationDto? = null
        val jsonString = "알림 없음"
        Mockito.`when`(objectMapper.writeValueAsString(CustomResponse(101, dto, jsonString))).thenReturn(jsonString)

        notificationService.sendNotification(userId, dto)

        Mockito.verify(emitterRepository, Mockito.times(1)).get(userId)
    }

    @Test
    fun `test markAsRead`() {
        val userId: Long = 1
        val notificationId: Long = 1
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")

        val notification = Notification(NotificationType.SURVEY, "content", 1, user, LocalDateTime.now())
        Mockito.`when`(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification))

        notificationService.markAsRead(userId, notificationId)

        assertTrue(notification.read)
    }

    @Test
    fun `test sendNotificationToAllEmitter`() {
        val userIds = mutableSetOf<Long>(1, 2, 3)
        Mockito.`when`(emitterRepository.getAllIds()).thenReturn(userIds)
        doNothing().`when`(notificationService).sendOldestUnreadNotificationToUser(any<Long>())

        notificationService.sendNotificationToAllEmitter()

        Mockito.verify(emitterRepository, Mockito.times(1)).getAllIds()
    }

    @Test
    fun `test sendOldestUnreadNotificationToUser`() {
        val userId: Long = 1
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val notification = Notification(NotificationType.SURVEY, "content", 1, user, LocalDateTime.now())
        Mockito.`when`(notificationRepository.findFirstByUserIdAndDeliveredFalseAndReservedAtLessThanOrderByIdAsc(eq(userId), any())).thenReturn(notification)
        doNothing().`when`(notificationService).sendNotification(eq(userId), any<NotificationDto>())
        notificationService.sendOldestUnreadNotificationToUser(userId)

        assertTrue(notification.delivered)
    }

    @Test
    fun `test getAllSent`() {
        val userId: Long = 1
        val user = User(id = userId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val notification1 = Notification(NotificationType.SURVEY, "content", 1, user, LocalDateTime.now().minusMinutes(1))
        val notification2 = Notification(NotificationType.SURVEY, "content", 1, user, LocalDateTime.now().minusMinutes(2))
        val notifications = listOf(notification1, notification2)
        Mockito.`when`(notificationRepository.findByUserIdAndReservedAtLessThanOrderByIdAsc(eq(userId), any())).thenReturn(notifications)

        val result = notificationService.getAllSent(userId)

        assertNotNull(result)
        assertEquals(notifications.size, result.size)
    }
}
