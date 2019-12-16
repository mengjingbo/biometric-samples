package com.biometric.samples.core

/**
 * date        ：2019/11/30
 * author      ：蒙景博
 * description ：
 */
enum class BiometricCompatMode {
    /**
     * 当前设备支持生物识别
     */
    BIOMETRIC_SUCCESS,
    /**
     * 当前设备不支持生物识别
     */
    BIOMETRIC_ERROR_HW_UNAVAILABLE,
    /**
     * 当前设备未启用生物识别
     */
    BIOMETRIC_ERROR_NONE_ENROLLED,
    /**
     * 当前设备无生物识别硬件
     */
    BIOMETRIC_ERROR_NO_HARDWARE,
    /**
     * 返回未知错误结果
     */
    BIOMETRIC_ERROR_UNKNOWN
}