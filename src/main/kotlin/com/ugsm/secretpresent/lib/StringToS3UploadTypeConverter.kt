package com.ugsm.secretpresent.lib

import com.ugsm.secretpresent.enums.S3ImageUploadType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.util.*


@Component
class StringToS3UploadTypeConverter : Converter<String, S3ImageUploadType> {
    override fun convert(source: String): S3ImageUploadType? {
        return try {
            S3ImageUploadType.valueOf(source.uppercase(Locale.getDefault()))
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
