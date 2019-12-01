package com.biometric.samples

/**
 * date        ：2019/11/30
 * author      ：蒙景博
 * description ：
 */
data class BiometricPromptInfo(
    /**
     * 必填：生物识别弹框标题
     */
    var title: String = "",
    /**
     * 可选：生物识别弹框副标题
     */
    var subtitle: String? = "",
    /**
     * 可选：生物识别弹框描述
     */
    var description: String? = "",
    /**
     * 必填：设置去取消按钮的文本。 但也可用于显示另一种身份验证方式，例如“输入备用密码”验证。
     */
    var negativeButtonText: String = "",
    /**
     * 可选：
     */
    var requireConfirmation: Boolean? = true,
    /**
     * 生物识别弹框副标题
     */
    var deviceCredentialAllowed: Boolean? = true,
    /**
     * 生物识别弹框副标题
     */
    var handlingDeviceCredentialResult: Boolean? = false
)