package com.sportz.match_center_base.helper

interface MatchCenterBaseConfigContract {
    fun getBaseUrl(): String
    fun getApiAuthKey(): String
    fun getIsDebugMode(): Boolean
}
