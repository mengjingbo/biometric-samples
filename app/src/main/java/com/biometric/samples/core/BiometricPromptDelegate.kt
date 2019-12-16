package com.biometric.samples.core

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
import com.biometric.samples.callback.OnBiometricAuthenticationCallback
import java.util.concurrent.Executor
import javax.crypto.Cipher

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
        savedInstanceState?.let { mCounter = savedInstanceState.getInt(KEY_COUNTER) }
    }

    fun onSaveInstanceState(@NonNull outState: Bundle) {
        outState.putInt(KEY_COUNTER, mCounter)
    }

    @NonNull
    abstract fun getApplicationContext(): Context?

    /**
     * 注册
     */
    abstract fun register(activity: FragmentActivity, callback: OnBiometricAuthenticationCallback)

    abstract fun register(fragment: Fragment, callback: OnBiometricAuthenticationCallback)

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

    /**
     * 启动生物识别认证
     */
    open fun startAuthentication(@NonNull info: BiometricPromptInfo) {
        mCounter++
        mBiometricPrompt?.authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle(info.title)
                .setSubtitle(info.subtitle)
                .setDescription(info.description)
                .setConfirmationRequired(true)
                .setDeviceCredentialAllowed(true)
                .build()
        )
    }

    /**
     * 启动生物识别认证，含密钥
     */
    open fun startAuthentication(@NonNull info: BiometricPromptInfo, @NonNull cipher: Cipher) {
        mCounter++
        mBiometricPrompt?.authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle(info.title)
                .setSubtitle(info.subtitle)
                .setDescription(info.description)
                .setNegativeButtonText(info.negativeButtonText)
                .build(), BiometricPrompt.CryptoObject(cipher)
        )
    }

    open fun onPush() {
        // 取消生物识别认证
        mBiometricPrompt?.cancelAuthentication()
    }
}