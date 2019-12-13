package com.biometric.samples

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

/**
 * date        ：2019/11/30
 * author      ：蒙景博
 * description ：
 */
abstract class BiometricPromptDelegate {

    companion object {
        private const val KEY_COUNTER = "saved_counter"
    }

    private var mCounter = 0

    var mBiometricPrompt: BiometricPrompt? = null

    open val mExecutor = Executor { Handler(Looper.getMainLooper()).post(it) }

    fun init(@Nullable savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            mCounter = savedInstanceState.getInt(KEY_COUNTER)
        }
    }

    fun onSaveInstanceState(@NonNull outState: Bundle) {
        outState.putInt(KEY_COUNTER, mCounter)
    }

    @NonNull
    abstract fun getApplicationContext(): Context?

    abstract fun registered(activity: FragmentActivity, callback: OnBiometricAuthenticationCallback)

    abstract fun registered(fragment: Fragment, callback: OnBiometricAuthenticationCallback)

    /**
     * 检测设备是否支持生物识别
     */
    open fun checkBiometricCompat(): BiometricCompatMode {
        return if (getApplicationContext() != null) {
            val mBiometricManager = getApplicationContext()?.let { BiometricManager.from(it) }
            when (mBiometricManager?.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> BiometricCompatMode.BIOMETRIC_SUCCESS_MESSAGE
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricCompatMode.BIOMETRIC_ERROR_NONE_ENROLLED
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricCompatMode.BIOMETRIC_ERROR_HW_UNAVAILABLE_MESSAGE
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricCompatMode.BIOMETRIC_ERROR_NO_HARDWARE
                else -> BiometricCompatMode.BIOMETRIC_ERROR_UNKNOWN
            }
        } else {
            BiometricCompatMode.BIOMETRIC_ERROR_UNKNOWN
        }
    }

    open fun startAuthentication(info: BiometricPromptInfo) {
        mCounter++
        mBiometricPrompt?.authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle(info.title)
                .setSubtitle(info.subtitle)
                .setDescription(info.description)
                .setConfirmationRequired(true)
                // .setNegativeButtonText(info.negativeButtonText)
                .setDeviceCredentialAllowed(true)
                .build()
        )
    }

    open fun onPush() {

    }
}