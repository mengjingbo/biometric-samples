package com.biometric.samples

import androidx.biometric.BiometricPrompt

/**
 * date        ：2019/12/13
 * author      ：蒙景博
 * description ：
 */
interface OnBiometricAuthenticationCallback {

    fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult)

    fun onAuthenticationError(errorCode: Int, errorMsg: CharSequence)

    fun onAuthenticationFailed(number: Int)
}