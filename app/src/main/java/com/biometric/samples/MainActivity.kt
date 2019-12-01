package com.biometric.samples

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val mBiometricController by lazy { BiometricPromptController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mTips = findViewById<TextView>(R.id.biometric_authentication_tips)
        mBiometricController.init(savedInstanceState)
        mBiometricController.connect(this)
        findViewById<ImageView>(R.id.biometric_authentication).setOnClickListener {
            when(mBiometricController.checkBiometricCompat()){
                BiometricCompatMode.BIOMETRIC_SUCCESS_MESSAGE -> {
                    mTips.text = ""
                    mBiometricController.startAuthentication(BiometricPromptInfo(title = "指纹识别", negativeButtonText = "使用密码"))
                }
                BiometricCompatMode.BIOMETRIC_ERROR_NONE_ENROLLED -> mTips.text = "指纹识别失败"
                BiometricCompatMode.BIOMETRIC_ERROR_HW_UNAVAILABLE_MESSAGE -> mTips.text = "未开启指纹识别"
                BiometricCompatMode.BIOMETRIC_ERROR_NO_HARDWARE -> mTips.text = "不支持指纹识别"
                else -> mTips.text = "未知错误"
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBiometricController.onSaveInstanceState(outState)
    }
}
