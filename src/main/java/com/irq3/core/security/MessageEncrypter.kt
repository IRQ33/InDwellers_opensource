package com.irq3.core.security

import com.irq3.infrastucture.EnvController
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object MessageEncrypter {
    private val key: String = EnvController.getData("KEY")

    fun encrypt(text: String): String{
        val keyBytes = key.toByteArray(Charsets.UTF_8)
        val secretKey = SecretKeySpec(keyBytes, "AES")

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(12)
        SecureRandom().nextBytes(iv)
        val spec = GCMParameterSpec(128, iv)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)
        val encrypted = cipher.doFinal(text.toByteArray(Charsets.UTF_8))

        val combined = iv + encrypted
        return Base64.getEncoder().encodeToString(combined)
    }

    fun decrypt(encryptedText: String): String {
        val keyBytes = key.toByteArray(Charsets.UTF_8)
        val secretKey = SecretKeySpec(keyBytes, "AES")

        val combined = Base64.getDecoder().decode(encryptedText)
        val iv = combined.copyOfRange(0, 12)
        val encrypted = combined.copyOfRange(12, combined.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        return String(cipher.doFinal(encrypted), Charsets.UTF_8)
    }


}