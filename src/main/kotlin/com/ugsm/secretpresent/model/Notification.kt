package com.ugsm.secretpresent.model

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class Notification(
    @Column
    var content: String,

    @Column
    var read: Boolean,

    @ManyToOne
    @JoinColumn(name="user_id")
    var user: User,

    @Column
    var reservedAt: LocalDateTime,

    @Id
    val id: Long,
):BaseTimeEntity()