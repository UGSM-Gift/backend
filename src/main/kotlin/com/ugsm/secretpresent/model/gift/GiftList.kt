package com.ugsm.secretpresent.model.gift

import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.model.UserAnniversary
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class GiftList(
    @ManyToOne
    @JoinColumn(name="user_anniversary_id")
    var userAnniversary: UserAnniversary,

    @ManyToOne
    @JoinColumn(name="taker_id")
    var taker: User,

    @Column
    var expiredAt: LocalDateTime,

    @Column
    var packageImgName: String,

    @OneToMany
    var categories: List<GiftListCategory>,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int
):BaseTimeEntity()