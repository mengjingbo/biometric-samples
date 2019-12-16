package com.biometric.samples

import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import com.biometric.samples.callback.OnBiometricAuthenticationCallback
import com.biometric.samples.core.BiometricCompatMode
import com.biometric.samples.core.BiometricPromptInfo
import com.biometric.samples.helper.BiometricPromptSecretKeyHelper
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.util.concurrent.atomic.AtomicBoolean
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException

class MainActivity : AppCompatActivity(), OnBiometricAuthenticationCallback {

    companion object {
        // 暂定密钥为BiometricCode
        private const val SECRET_KEY = "BiometricCode"
        private const val SECRET_MESSAGE = "生物识别认证"
    }

    private lateinit var mTips: TextView

    private val mBiometricController by lazy { BiometricPromptController(this) }

    // 标记是否使用SecretKey
    private val mUsedSecretKeyMarker = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTips = findViewById(R.id.biometric_authentication_tips)
        mBiometricController.init(savedInstanceState)
        mBiometricController.register(this, this)
        findViewById<ImageView>(R.id.biometric_authentication).setOnClickListener {
            checkBiometricCompat()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mBiometricController.onSaveInstanceState(outState)
    }

    override fun onPause() {
        mBiometricController.onPush()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_secret_key -> {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val mSecretKey = BiometricPromptSecretKeyHelper.generateBiometricBoundKey(SECRET_KEY, true)
                        Log.e(this.javaClass.simpleName, "密钥生成成功：${mSecretKey?.toString()}")
                    }else {
                        Log.e(this.javaClass.simpleName, "密钥生成失败：当前设备版本不支持密钥生成")
                    }
                } catch (e: InvalidAlgorithmParameterException) {
                    Log.e(this.javaClass.simpleName, "密钥生成失败：${e.message}")
                } catch (e: NoSuchAlgorithmException) {
                    Log.e(this.javaClass.simpleName, "密钥生成失败：${e.message}")
                } catch (e: NoSuchProviderException) {
                    Log.e(this.javaClass.simpleName, "密钥生成失败：${e.message}")
                }
                true
            }
            R.id.used_secret_key -> {
                item.isChecked = !item.isChecked
                mUsedSecretKeyMarker.lazySet(item.isChecked)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * 检测设备是否支持生物识别
     */
    private fun checkBiometricCompat(){
        when (mBiometricController.checkBiometricCompat()) {
            BiometricCompatMode.BIOMETRIC_SUCCESS -> {
                // 设备支持生物识别功能，启动生物识别认证
                mTips.text = ""
                startAuthentication()
            }
            BiometricCompatMode.BIOMETRIC_ERROR_NONE_ENROLLED -> mTips.text = "生物识别未启用"
            BiometricCompatMode.BIOMETRIC_ERROR_HW_UNAVAILABLE -> mTips.text = "当前设备不支持生物识别"
            BiometricCompatMode.BIOMETRIC_ERROR_NO_HARDWARE -> mTips.text = "当前设备不支持生物识别"
            else -> mTips.text = "未知错误"
        }
    }

    /**
     * 启动生物识别
     */
    private fun startAuthentication() {
        if (!mUsedSecretKeyMarker.get()) {
            mBiometricController.startAuthentication(
                BiometricPromptInfo(
                    "生物识别"
                )
            )
        }else {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val mCipher = BiometricPromptSecretKeyHelper.getCipher()
                    val mSecretKey = BiometricPromptSecretKeyHelper.getSecretKey(SECRET_KEY)
                    if (mCipher != null && mSecretKey != null) {
                        mCipher.init(Cipher.ENCRYPT_MODE, mSecretKey)
                        mBiometricController.startAuthentication(BiometricPromptInfo(title = "生物识别", negativeButtonText = "使用密码"), mCipher)
                    }
                } else {
                    Log.e(this.javaClass.simpleName, "生物识别认证异常：当前设备版本不支持密钥读取")
                }
            } catch (e: NoSuchPaddingException) {
                Log.e(this.javaClass.simpleName, "生物识别认证异常：${e.message}")
            } catch (e: NoSuchAlgorithmException) {
                Log.e(this.javaClass.simpleName, "生物识别认证异常：${e.message}")
            } catch (e: KeyStoreException) {
                Log.e(this.javaClass.simpleName, "生物识别认证异常：${e.message}")
            } catch (e: IOException) {
                Log.e(this.javaClass.simpleName, "生物识别认证异常：${e.message}")
            } catch (e: UnrecoverableKeyException) {
                Log.e(this.javaClass.simpleName, "生物识别认证异常：${e.message}")
            } catch (e: CertificateException) {
                Log.e(this.javaClass.simpleName, "生物识别认证异常：${e.message}")
            } catch (e: InvalidKeyException) {
                Log.e(this.javaClass.simpleName, "生物识别认证异常：${e.message}")
            } catch (e: IllegalArgumentException) {
                Log.e(this.javaClass.simpleName, "生物识别认证异常：${e.message}")
            }
        }
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        if (mUsedSecretKeyMarker.get()) {
            mTips.text = Base64.encodeToString(result.cryptoObject?.cipher?.doFinal(SECRET_MESSAGE.toByteArray()), Base64.DEFAULT)
        } else {
            mTips.text = "生物识别成功"
        }
    }

    override fun onAuthenticationError(errorCode: Int, errorMsg: CharSequence) {
        if (mUsedSecretKeyMarker.get()) {
            if (errorCode == BiometricConstants.ERROR_NEGATIVE_BUTTON) {
                mTips.text = "弹出自定义的密码认证框"
            } else {
                mTips.text = String.format("生物识别错误，错误信息：%s", errorMsg)
            }
        } else {
            mTips.text = String.format("生物识别错误，错误信息：%s", errorMsg)
        }
    }

    override fun onAuthenticationFailed(number: Int) {
        mTips.text = String.format("生物识别失败，失败%s次", number)
    }
}
