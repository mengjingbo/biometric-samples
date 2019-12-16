package com.biometric.samples.core

/**
 * date        ：2019/11/30
 * author      ：蒙景博
 * description ：
 */
enum class BiometricCompatMode {
    /**
     * 生物识别成功
     */
    BIOMETRIC_SUCCESS_MESSAGE,
    /**
     * 设备未开启生物识别
     */
    BIOMETRIC_ERROR_HW_UNAVAILABLE_MESSAGE,
    /**
     * 生物识别失败
     */
    BIOMETRIC_ERROR_NONE_ENROLLED,
    /**
     * 设备无生物识别硬件
     */
    BIOMETRIC_ERROR_NO_HARDWARE,
    /**
     * 返回未知错误结果
     */
    BIOMETRIC_ERROR_UNKNOWN
}