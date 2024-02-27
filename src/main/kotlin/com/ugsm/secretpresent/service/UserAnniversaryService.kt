package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.CreateUserAnniversaryDto
import com.ugsm.secretpresent.dto.UserAnniversaryDto
import com.ugsm.secretpresent.model.UserAnniversary
import com.ugsm.secretpresent.repository.AnniversaryImageRepository
import com.ugsm.secretpresent.repository.UserAnniversaryRepository
import com.ugsm.secretpresent.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class UserAnniversaryService(
    @Autowired
    val userAnniversaryRepository: UserAnniversaryRepository,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val anniversaryImageRepository: AnniversaryImageRepository
) {

    fun findByYearMonth(userId: Long, yearMonth: YearMonth): List<UserAnniversaryDto> {

        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()


        val userAnniversaries = userAnniversaryRepository.findByUserIdAndDateBetween(userId, startDate, endDate)

        return userAnniversaries.map { UserAnniversaryDto(
            it.id, it.name, it.date, it.image.imageUrl
        ) }
    }

    fun create(userId:Long, dto: CreateUserAnniversaryDto){
        val anniversary = UserAnniversary(
            userRepository.findById(userId).get(),
            anniversaryImageRepository.findById(dto.imageId).get(),
            dto.name,
            dto.date
        )

        userAnniversaryRepository.save(anniversary)
    }

    fun delete(userId: Long, anniversaryId: Int){
        val anniversary = userAnniversaryRepository.findById(anniversaryId).get()

        if (anniversary.user.id != userId) {
            throw UnauthorizedException(message = "You are not allowed to do this action.")
        }

        userAnniversaryRepository.delete(anniversary)
    }
}