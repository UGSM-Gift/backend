package com.ugsm.secretpresent

import com.ugsm.secretpresent.service.UserService
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableEncryptableProperties
@EnableJpaAuditing
class SecretpresentApplication

fun main(args: Array<String>) {
	runApplication<SecretpresentApplication>(*args)
}
