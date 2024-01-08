package com.ugsm.secretpresent.model

import jakarta.persistence.*

@Entity
class AnniversaryImage(
    @Column
    var imageUrl: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int
) :BaseTimeEntity()