package com.ugsm.secretpresent

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

//@ActiveProfiles("local")
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = [SecretpresentApplication::class])
@EnableEncryptableProperties
class JpaTest(
) {

    @Test
    fun test() {
    }
}