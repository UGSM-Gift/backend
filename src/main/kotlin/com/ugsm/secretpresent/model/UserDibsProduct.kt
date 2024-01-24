package com.ugsm.secretpresent.model

import com.ugsm.secretpresent.model.product.Product
import jakarta.persistence.*

@Entity
class UserDibsProduct(
    @ManyToOne
    @JoinColumn(name="user_id")
    var user: User,

    @ManyToOne
    @JoinColumn(name="product_id")
    var product: Product,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
): BaseTimeEntity()
