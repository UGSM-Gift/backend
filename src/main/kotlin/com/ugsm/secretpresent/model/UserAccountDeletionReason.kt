package com.ugsm.secretpresent.model

import jakarta.persistence.*

@Entity
class UserAccountDeletionReason(
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    var user: User,


    @ManyToOne
    @JoinColumn(name = "deletion_reason_id")
    var deletionReason: AccountDeletionReason,

    @Column
    var details: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
):BaseTimeEntity()