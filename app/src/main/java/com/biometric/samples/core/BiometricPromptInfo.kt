package com.biometric.samples.core

/**
 * date        ：2019/11/30
 * author      ：蒙景博
 * description ：
 */
data class BiometricPromptInfo(
    /**
     * 必填：生物识别弹框标题
     */
    var title: String,
    /**
     * 可选：生物识别弹框副标题
     */
    var subtitle: String = "",
    /**
     * 可选：生物识别弹框提示
     */
    var description: String = "",
    /**
     * 认证方式一：单纯的生物识别认证和密码认证无需填写
     * 认证方式二：使用密钥与生物识别绑定认证时必填,
     */
    var negativeButtonText: String = ""
)