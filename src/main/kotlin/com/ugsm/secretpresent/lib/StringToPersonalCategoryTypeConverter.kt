package com.ugsm.secretpresent.lib

import com.ugsm.secretpresent.enums.PersonalCategoryType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.util.*

@Component
class StringToPersonalCategoryTypeConverter: Converter<String, PersonalCategoryType> {
    override fun convert(source: String): PersonalCategoryType? {
        return try {
            PersonalCategoryType.valueOf(source.uppercase(Locale.getDefault()))
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
