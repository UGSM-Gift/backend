package com.ugsm.secretpresent.lib

import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.util.*

@Component
class StringToGiftConfirmedStatusConverter : Converter<String, GiftConfirmedStatus> {
    override fun convert(source: String): GiftConfirmedStatus? {
        return try {
            GiftConfirmedStatus.valueOf(source.uppercase(Locale.getDefault()))
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
