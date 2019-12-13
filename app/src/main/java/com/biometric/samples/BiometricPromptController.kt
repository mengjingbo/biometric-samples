package com.biometric.samples

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * date        ：2019/11/30
 * author      ：蒙景博
 * description ：
 */
class BiometricPromptController(private val context: Context?) : BiometricPromptDelegate() {

    override fun getApplicationContext(): Context? = context

    /**
     * 在FragmentActivity中注册
     */
    override fun registered(activity: FragmentActivity, callback: OnBiometricAuthenticationCallback) {
        mBiometricPrompt = BiometricPrompt(activity, mExecutor, OnAuthenticationCallback(callback))
    }

    /**
     * 在Fragment中注册
     */
    override fun registered(fragment: Fragment, callback: OnBiometricAuthenticationCallback) {
        mBiometricPrompt = BiometricPrompt(fragment, mExecutor, OnAuthenticationCallback(callback))
    }

    internal inner class OnAuthenticationCallback(private val callback: OnBiometricAuthenticationCallback?) : BiometricPrompt.AuthenticationCallback() {

        // 记录失败次数
        private var mNumberFailedAttempts: Int = 0

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            mNumberFailedAttempts = 0
            callback?.onAuthenticationSucceeded(result)
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            mNumberFailedAttempts = 0
            callback?.onAuthenticationError(errorCode, errString)
        }

        override fun onAuthenticationFailed() {
            mNumberFailedAttempts++
            callback?.onAuthenticationFailed(mNumberFailedAttempts)
        }
    }
}