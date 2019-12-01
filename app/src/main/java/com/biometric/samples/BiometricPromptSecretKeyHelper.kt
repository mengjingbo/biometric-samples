package com.biometric.samples

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
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
     *
     */
    fun generateBiometricBoundKey(keyName: String, invalidatedByBiometricEnrollment: Boolean) {
        generateKey(keyName, true, invalidatedByBiometricEnrollment, -1)
    }

    /**
     *
     */
    fun generateCredentialBoundKey(keyName: String, validityDuration: Int) {
        generateKey(keyName = keyName, validityDuration = validityDuration)
    }

    private fun generateKey(keyName: String, biometricBound: Boolean = false, invalidatedByBiometricEnrollment: Boolean = false, validityDuration: Int) {
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
        mKeyGenerator.generateKey()
    }

    fun getSecretKey(keyName: String): SecretKey {
        val mKeyStore = KeyStore.getInstance("AndroidKeyStore")
        mKeyStore.load(null)
        return mKeyStore.getKey(keyName, null) as SecretKey
    }

    fun getCipher(): Cipher =
        Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
}