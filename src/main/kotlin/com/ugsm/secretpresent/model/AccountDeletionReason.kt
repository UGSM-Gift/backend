package com.ugsm.secretpresent.model

import jakarta.persistence.*

@Entity
class AccountDeletionReason(
    @Column
    val name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
):BaseTimeEntity()