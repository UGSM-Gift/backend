package com.ugsm.secretpresent.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
class VerificationMessage(

    @Column(nullable = false)
    var code: Int,

    @Column(nullable = false)
    var receiverPhoneNumber: String,

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    var user: User,

    @Column
    var isConfirmed: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) : BaseTimeEntity()