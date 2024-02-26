package com.ugsm.secretpresent.config

import com.ugsm.secretpresent.lib.StringToPersonalCategoryTypeConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry

@Configuration
class WebConfig {
    fun addFormatters(registry:FormatterRegistry){
        registry.addConverter(StringToPersonalCategoryTypeConverter())
    }
}