package com.ugsm.secretpresent.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    @Autowired
    val nofiticationService: NotificationService
) {

    @Scheduled(fixedDelay = 1000)
    fun sendNotification() = nofiticationService.notify(1, "data: hehehehe")

}