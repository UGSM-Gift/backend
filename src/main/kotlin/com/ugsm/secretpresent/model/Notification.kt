package com.ugsm.secretpresent.model

import com.ugsm.secretpresent.enums.NotificationType
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class Notification(
    @Column
    @Enumerated(EnumType.STRING)
    var type: NotificationType,

    @Column
    var content: String,

    @Column
    var referenceId: Int?,

    @ManyToOne
    @JoinColumn(name="user_id")
    var user: User,

    @Column
    var reservedAt: LocalDateTime,

    @Column
    var read: Boolean = false,

    @Column
    var delivered: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    ):BaseTimeEntity()