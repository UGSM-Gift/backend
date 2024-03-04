package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.UserActivity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class UserActivityService(
    @Autowired val mongoTemplate: MongoTemplate
) {
    fun logActivity(userId: Long?, endpoint: String?, method: String?) {
        val activity = UserActivity(userId, endpoint, method, LocalDateTime.now())
        val currentDate = LocalDate.now().toString()
        mongoTemplate.insert(activity, currentDate) // "user_activities" is collection name
    }

}
