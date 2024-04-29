package com.ugsm.secretpresent.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class UserAnniversary(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne
    @JoinColumn(name = "image_id")
    var image: AnniversaryImage,

    @Column
    var name: String,

    @Column
    var date: LocalDate,

    @Column
    var deleted: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

) :BaseTimeEntity()