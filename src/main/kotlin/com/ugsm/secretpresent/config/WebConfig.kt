package com.ugsm.secretpresent.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ugsm.secretpresent.lib.StringToGiftConfirmedStatusConverter
import com.ugsm.secretpresent.lib.StringToPersonalCategoryTypeConverter
import com.ugsm.secretpresent.lib.StringToS3UploadTypeConverter
import com.ugsm.secretpresent.lib.UserActivityInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.text.SimpleDateFormat

@Configuration
class WebConfig(
    @Autowired val userActivityInterceptor: UserActivityInterceptor
): WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        super.addInterceptors(registry)
        registry.addInterceptor(userActivityInterceptor)
    }

    override fun addFormatters(registry:FormatterRegistry){
        registry.addConverter(StringToPersonalCategoryTypeConverter())
        registry.addConverter(StringToGiftConfirmedStatusConverter())
        registry.addConverter(StringToS3UploadTypeConverter())
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModules(JavaTimeModule())
        mapper.registerKotlinModule()
        return mapper
    }
}