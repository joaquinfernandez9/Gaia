package com.example.uigaiav2.domain.usecases.crypt

import android.app.Activity
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoManager {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    private val encryptCipher
        get() = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }

    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    "secret", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setKeySize(KEY_SIZE * 8).setBlockModes(BLOCK_MODE).setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false).setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(bytes: ByteArray, outputStream: OutputStream): ByteArray {
        val cipher = encryptCipher
        val iv = cipher.iv
        outputStream.use {
            it.write(iv)
            val inputStream = ByteArrayInputStream(bytes)
            val buffer = ByteArray(CHUNK_SIZE)
            while (inputStream.available() > CHUNK_SIZE) {
                inputStream.read(buffer)
                val encryptedBytes = cipher.doFinal(buffer)
                it.write(encryptedBytes)
            }
            val remainingBytes = inputStream.readBytes()
            val encryptedBytes = cipher.doFinal(remainingBytes)
            it.write(encryptedBytes)

        }
        return bytes
    }

    fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val iv = ByteArray(KEY_SIZE)
            it.read(iv)
            val cipher = getDecryptCipherForIv(iv)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(CHUNK_SIZE)
            while (inputStream.available() > CHUNK_SIZE) {
                inputStream.read(buffer)
                val ciphertextChunk = cipher.update(buffer)
                outputStream.write(ciphertextChunk)
            }
            val remainingBytes = inputStream.readBytes()
            val lastChunk = cipher.doFinal(remainingBytes)
            outputStream.write(lastChunk)
            outputStream.toByteArray()
        }
    }

    companion object {
        private const val CHUNK_SIZE = 1024 * 4
        private const val KEY_SIZE = 16
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

    fun readTxt(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(
            "sharedPrefs", Context.MODE_PRIVATE
        )
        val secret = File(context.filesDir, "secret.txt")
        return when {
            sharedPreferences.contains("username") -> {
                sharedPreferences.getString("username", "").toString()
            }
            secret.exists() -> {
                val fis = FileInputStream(secret)
                val bytes = decrypt(inputStream = fis)
                val response = bytes.decodeToString()
                val list = response.split(" ")
                list[0]
            }
            else -> {
                showSnackbar(context, "Can't load username. Try again later.")
                ""
            }
        }
    }

    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val secret = File(context.filesDir, "secret.txt")
        sharedPreferences.edit().remove("username").apply()
        if (secret.exists()) {
            secret.delete()
        }
        showSnackbar(context, "Logout successful")
    }

    private fun showSnackbar(context: Context, message: String) {
        val rootView = (context as? Activity)?.findViewById<View>(android.R.id.content) ?: return
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }
}