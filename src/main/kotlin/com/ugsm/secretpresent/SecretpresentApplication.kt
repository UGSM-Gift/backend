package com.ugsm.secretpresent

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableEncryptableProperties
@EnableJpaAuditing
class SecretpresentApplication{

}

fun main(args: Array<String>) {
    runApplication<SecretpresentApplication>(*args)
}
