package com.biometric.samples.helper

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * date        ：2019/12/1
 * author      ：蒙景博
 * description ：生成密钥和获取密钥
 */
@RequiresApi(api = Build.VERSION_CODES.M)
object BiometricPromptSecretKeyHelper {

    /**
     * 生成生物识别绑定的密钥
     */
    fun generateBiometricBoundKey(keyName: String, invalidatedByBiometricEnrollment: Boolean): Key? {
        return generateKey(
            keyName,
            true,
            invalidatedByBiometricEnrollment,
            -1
        )
    }

    /**
     * 生成生物识别绑定的密钥
     */
    fun generateCredentialBoundKey(keyName: String, validityDuration: Int): Key? {
        return generateKey(
            keyName = keyName,
            validityDuration = validityDuration
        )
    }

    private fun generateKey(keyName: String, biometricBound: Boolean = false, invalidatedByBiometricEnrollment: Boolean = false, validityDuration: Int): Key? {
        val mBuilder = KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
        if (biometricBound) {
            mBuilder.setUserAuthenticationValidityDurationSeconds(-1)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mBuilder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment)
            }
        } else {
            mBuilder.setUserAuthenticationValidityDurationSeconds(validityDuration)
        }
        val mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore")
        mKeyGenerator.init(mBuilder.build())
        return mKeyGenerator.generateKey()
    }

    fun getSecretKey(keyName: String): SecretKey? {
        val mKeyStore = KeyStore.getInstance("AndroidKeyStore")
        mKeyStore.load(null)
        return mKeyStore.getKey(keyName, null) as SecretKey
    }

    fun getCipher(): Cipher? = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
}