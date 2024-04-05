package dev.kr3st1k.piucompanion.core.helpers

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Crypto {
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    private val iv = IvParameterSpec(ByteArray(16))
    fun encryptData(data: String): String? {
        val key = SecretKeySpec(Utils.getAndroidId().toByteArray(), "AES")
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val res = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(res, Base64.URL_SAFE)
    }

    fun decryptData(data: String): String {
        val encrypted = Base64.decode(data, Base64.URL_SAFE)
        val key = SecretKeySpec(Utils.getAndroidId().toByteArray(), "AES")
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        return String(cipher.doFinal(encrypted))
    }
}