package com.ugsm.secretpresent.model

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class Notification(
    @Column
    var redirectUrl: String?,

    @Column
    var content: String,

    @ManyToOne
    @JoinColumn(name="user_id")
    var user: User,

    @Column
    var reservedAt: LocalDateTime,

    @Column
    var read: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

):BaseTimeEntity()