package com.kr3st1k.pumptracker.core.helpers

import com.kr3st1k.pumptracker.decryptAES
import com.kr3st1k.pumptracker.encryptAES

object Crypto {
    fun encryptData(data: String): String {
        return encryptAES(data)
    }

    fun decryptData(data: String): String {
        return decryptAES(data)
    }
}