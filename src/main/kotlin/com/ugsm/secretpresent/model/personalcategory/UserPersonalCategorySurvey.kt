package com.ugsm.secretpresent.model.personalcategory

import com.ugsm.secretpresent.model.User
import jakarta.persistence.*

@Entity
class UserPersonalCategorySurvey(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

)