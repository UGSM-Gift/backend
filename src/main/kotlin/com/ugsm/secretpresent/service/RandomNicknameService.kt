package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.model.RandomNickname
import com.ugsm.secretpresent.repository.RandomNicknameRepository
import com.ugsm.secretpresent.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class RandomNicknameService(
    @Autowired
    val randomNicknameRepository: RandomNicknameRepository,
    @Autowired
    val userRepository: UserRepository
) {
    fun getAutoGeneratedNickname(): String {
        val possibleNicknameNums = randomNicknameRepository.count()
        var notFound = true
        val candidate: RandomNickname

        while(true){
            val randomId = (1..possibleNicknameNums).random().toInt()
            candidate = randomNicknameRepository.findFirstById(randomId) ?: continue
            break
        }

        while(true){
            val nickname = "${candidate.name}${this.getRandomNicknameSuffix()}"
            if(userRepository.findByNickname(nickname) == null) return nickname
        }
    }

    private fun getRandomNicknameSuffix(): Int {
        return (100..999).random()
    }
}