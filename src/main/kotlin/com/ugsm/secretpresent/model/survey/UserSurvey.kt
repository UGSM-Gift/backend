package com.ugsm.secretpresent.model.survey

import com.ugsm.secretpresent.model.BaseTimeEntity
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.model.UserAnniversary
import jakarta.persistence.*

@Entity
class UserSurvey(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne
    @JoinColumn(name = "anniversary_id")
    var anniversary: UserAnniversary,

    @OneToMany(mappedBy = "survey")
    val answers: List<SurveyPersonalCategory> = emptyList(),

    ) : BaseTimeEntity()