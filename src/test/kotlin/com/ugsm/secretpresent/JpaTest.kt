package com.ugsm.secretpresent

import com.ugsm.secretpresent.repository.TestRepository
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

//@ActiveProfiles("local")
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = [SecretpresentApplication::class])
@EnableEncryptableProperties
class JpaTest(
    @Autowired
    var testRepository: TestRepository
) {

    @Test
    fun test() {
        val result = testRepository.test()
        println(result)
    }
}