package com.biometric.samples

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt

class MainActivity : AppCompatActivity(), OnBiometricAuthenticationCallback {

    private lateinit var mTips: TextView

    private val mBiometricController by lazy { BiometricPromptController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTips = findViewById(R.id.biometric_authentication_tips)
        mBiometricController.init(savedInstanceState)
        mBiometricController.registered(this, this)
        findViewById<ImageView>(R.id.biometric_authentication).setOnClickListener {
            when(mBiometricController.checkBiometricCompat()){
                BiometricCompatMode.BIOMETRIC_SUCCESS_MESSAGE -> {
                    mTips.text = ""
                    mBiometricController.startAuthentication(BiometricPromptInfo(title = "生物识别", negativeButtonText = "使用密码"))
                }
                BiometricCompatMode.BIOMETRIC_ERROR_NONE_ENROLLED -> mTips.text = "生物识别失败"
                BiometricCompatMode.BIOMETRIC_ERROR_HW_UNAVAILABLE_MESSAGE -> mTips.text = "未开启生物识别"
                BiometricCompatMode.BIOMETRIC_ERROR_NO_HARDWARE -> mTips.text = "不支持生物识别"
                else -> mTips.text = "未知错误"
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBiometricController.onSaveInstanceState(outState)
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        runOnUiThread {
            mTips.text = "生物识别成功"
        }
    }

    override fun onAuthenticationError(errorCode: Int, errorMsg: CharSequence) {
        runOnUiThread {
            mTips.text = String.format("生物识别错误，错误信息：%s", errorMsg)
        }
    }

    override fun onAuthenticationFailed(number: Int) {
        runOnUiThread {
            mTips.text = String.format("生物识别失败，失败%s次", number)
        }
    }
}
