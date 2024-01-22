package com.ugsm.secretpresent.config

import org.springframework.context.annotation.Bean
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule
import org.springframework.context.annotation.Configuration

@Configuration
class HibernateConfig {
    @Bean
    fun hibernate5Module(): Module = Hibernate5JakartaModule()
}