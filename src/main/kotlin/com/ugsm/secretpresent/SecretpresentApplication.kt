package com.ugsm.secretpresent

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*


@SpringBootApplication
@EnableEncryptableProperties
@EnableJpaAuditing
@EnableScheduling
class SecretpresentApplication

fun main(args: Array<String>) {
    runApplication<SecretpresentApplication>(*args)
}

@PostConstruct
fun init() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
}
