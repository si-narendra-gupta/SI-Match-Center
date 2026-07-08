package com.sportz.base.helper

interface BaseConfigContract {
    fun getBaseUrl(): String
    fun getApiAuthKey(): String
    fun getPreferenceDataStoreName(): String
    fun getIsDebugMode(): Boolean
}
