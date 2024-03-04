package com.ugsm.secretpresent.repository

import com.ugsm.secretpresent.model.AnniversaryImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnniversaryImageRepository:JpaRepository<AnniversaryImage, Int>