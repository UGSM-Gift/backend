package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.RandomNickname
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface RandomNicknameRepository:JpaRepository<RandomNickname, Int> {

    fun findFirstById(id: Int): RandomNickname?
}