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
class BiometricPromptController(private val context: Context): BiometricPromptDelegate() {



    override fun getApplicationContext(): Context? = context

    /**
     * 在FragmentActivity
     */
    override fun connect(activity: FragmentActivity) {
        mBiometricPrompt = BiometricPrompt(activity, mExecutor, OnAuthenticationCallback())
    }

    override fun connect(fragment: Fragment) {
        mBiometricPrompt = BiometricPrompt(fragment, mExecutor, OnAuthenticationCallback())
    }

    inner class OnAuthenticationCallback: BiometricPrompt.AuthenticationCallback() {
        // 失败次数
        private var mNumberFailedAttempts: Int = 0

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            mNumberFailedAttempts = 0
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            mNumberFailedAttempts = 0
        }

        override fun onAuthenticationFailed() {
            mNumberFailedAttempts++
        }
    }
}