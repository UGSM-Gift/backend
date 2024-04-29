package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.enums.NotificationType
import com.ugsm.secretpresent.model.Notification
import com.ugsm.secretpresent.repository.GiftListLetterRepository
import com.ugsm.secretpresent.repository.GiftListRepository
import com.ugsm.secretpresent.repository.NotificationRepository
import com.ugsm.secretpresent.repository.UserAnniversaryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SchedulerService(
    @Autowired
    val notificationService: NotificationService,
    @Autowired
    val anniversaryRepository: UserAnniversaryRepository,
    @Autowired
    val notificationRepository: NotificationRepository,
    @Autowired
    val giftListRepository: GiftListRepository,
    @Autowired
    val giftListLetterRepository: GiftListLetterRepository
) {

    @Scheduled(cron = "0 55 17 * * *")
    fun createNotificationForAnniversaryBeforeOneWeek() {
        val template = "\uD83C\uDF81%s까지 일주일! 선물로 뭘 받아야하지?\n" +
                "선물 고르기 어렵다면?  추천 받아보자!"
        val redirectUrl = "https://www.ugsm.co.kr/test"
        val date = LocalDate.now().plusWeeks(1)
        val targetAnniversaries = anniversaryRepository.findByDateAndDeletedFalse(date)
        val notifications = targetAnniversaries.map {
            Notification(
                type=NotificationType.SURVEY,
                referenceId = null,
                content = template.format(it.name),
                reservedAt = LocalDateTime.now().withHour(18),
                user = it.user
            )
        }
        notificationRepository.saveAll(notifications)
    }

    @Scheduled(cron = "0 55 07 * * *")
    fun createNotificationForAnniversaryDueDate() {
        val template = "\uD83C\uDF89%s을/를 맞이했어요! 받고 싶은 선물은 정하셨나요?\n" +
                "아직 못 골랐다면? %s님 마음에 딱! 드는 선물 찾아봐요"
        val redirectUrl = "https://www.ugsm.co.kr/test"
        val date = LocalDate.now()
        val targetAnniversaries = anniversaryRepository.findByDateAndDeletedFalse(date)
        val notifications = targetAnniversaries.map {
            Notification(
                type=NotificationType.SURVEY,
                referenceId = null,
                content = template.format(it.name, it.user.nickname),
                reservedAt = LocalDateTime.now().withHour(8),
                user = it.user
            )
        }
        notificationRepository.saveAll(notifications)
    }

    @Scheduled(cron = "0 55 22 * * *")
    fun createNotificationForGiftListExpiredBeforeOneHour() {
        val template = "⏰%s선물을 모두 받았나요?\n" + "선물 받기까지 1시간 남았어요!"
        val redirectUrl = "https://www.ugsm.co.kr/test"
        val date1 = LocalDateTime.now()
        val date2 = date1.plusHours(1)
        val giftList = giftListRepository.findByExpiredAtBetween(date1, date2)
        val notifications = giftList.map {
            Notification(
                type = NotificationType.GIFT_LIST,
                referenceId = it.id,
                content = template.format(it.userAnniversary.name),
                reservedAt = LocalDateTime.now().withHour(23),
                user = it.userAnniversary.user
            )
        }
        notificationRepository.saveAll(notifications)
    }

    @Scheduled(cron = "0 55 23 * * *")
    fun createNotificationForGiftListExpiredNow() {
        val template = "\uD83D\uDC4D선물 받기 완료! 선물을 모두 받으셨나요?\n" + "친구에게 받은 선물을 확인해보세요!"
        val redirectUrl = "https://www.ugsm.co.kr/test"
        val date1 = LocalDateTime.now()
        val date2 = date1.plusHours(1)
        val giftList = giftListRepository.findByExpiredAtBetween(date1, date2)
        val notifications = giftList.map {
            Notification(
                type = NotificationType.GIFT_LIST,
                referenceId = it.id,
                content = template,
                reservedAt = LocalDateTime.now().withHour(23).withMinute(59).withSecond(0),
                user = it.userAnniversary.user
            )
        }
        notificationRepository.saveAll(notifications)
    }

    @Scheduled(cron = "0 */2 * * * *")
    fun createNotificationForConfirmedLetter() {
        val template = "\uD83D\uDD90\uFE0F잠깐! %s을/를 받은것이 확실하나요?\n" + "미확인시 해당 선물을 받지 못할 수 있어요"
        val redirectUrl = "https://www.ugsm.co.kr/test"
        val date1 = LocalDateTime.now()
        val date2 = date1.plusMinutes(2)
        val letters = giftListLetterRepository.findByConfirmedStatusAndCreatedAtBetween(
            GiftConfirmedStatus.NOT_CONFIRMED,
            date1,
            date2
        )
        val notifications = letters.map {
            Notification(
                type = NotificationType.GIFT_LIST_LETTER,
                referenceId = it.id,
                content = template.format(it.productCategoryName),
                reservedAt = date1.plusMinutes(1),
                user = it.receiver
            )
        }
        notificationRepository.saveAll(notifications)
    }

    @Scheduled(fixedRate = 60000)
    fun sendNotification() {
        notificationService.sendNotificationToAllEmitter()
    }
}