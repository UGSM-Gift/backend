package com.ugsm.secretpresent.lib

class PhoneNoUtils {
    companion object{
        fun remainNumberOnly(phoneNumber:String?) = phoneNumber?.replace(Regex("[^0-9]"), "")
        fun addDash(phoneNumber:String): String {
            val regex = Regex("(^02|^031|^032|^033|^041|^042|^043|^044|^051|^052|^053|^054|^055|^061|^062|^063|^064|^067|^0\\d{2})(\\d{3,4})(\\d{4})")
            return phoneNumber.replace(regex,"$1-$2-$3")
        }
        fun validateNumberOnlyFormat(phoneNumber: String) = phoneNumber.length in 9..11
    }
}